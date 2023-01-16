package org.waraccademy.warcorpses.database.manager;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.reactivestreams.Publisher;
import org.waraccademy.mongo.database.IMongoDBManager;
import org.waraccademy.mongo.database.subscribers.ObservableSubscriber;
import org.waraccademy.warcorpses.WarCorpses;
import org.waraccademy.warcorpses.database.object.CorpseLog;
import org.waraccademy.warcorpses.database.subscriber.CompletableSubscriber;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DatabaseManager {
    private final ConnectorManager manager = WarCorpses.getInstance().getMongo();

    private final YamlConfiguration config = WarCorpses.getInstance().getConfig();
    private MongoCollection<Document> collection;

    public DatabaseManager() {
        manager.collectionExist("corpses").whenComplete((exists, e) -> {
            if (e == null) {
                if (exists) {
                    collection = manager.getCollection("corpses");
                } else {
                    collection = manager.createCollectionAndGet("corpses");
                    Publisher<String> pub = collection.createIndex(Indexes.descending("date"),
                            new IndexOptions().expireAfter(config.getLong("database.log-ttl"), TimeUnit.DAYS));

                    pub.subscribe(new ObservableSubscriber<>());
                }
            }
        });
    }

    public void saveLog(CorpseLog log){
        if(log.getItems().isEmpty())
            return;

        collection.insertOne(log.getDocument()).subscribe(new ObservableSubscriber<>());
    }

    public CompletableFuture<List<CorpseLog>> getLogs(String player){
        CompletableSubscriber<Document,List<CorpseLog>> subscriber = new CompletableSubscriber<>(o -> o.stream()
                .map(CorpseLog::of)
                .collect(Collectors.toList()));

        collection.find(Filters.eq("player",player)).subscribe(subscriber);


        return subscriber.getResult();
    }
}