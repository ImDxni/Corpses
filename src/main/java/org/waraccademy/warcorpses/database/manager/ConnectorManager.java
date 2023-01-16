package org.waraccademy.warcorpses.database.manager;

import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.waraccademy.mongo.MongoPlugin;
import org.waraccademy.mongo.database.builder.MongoSettingsBuilder;
import org.waraccademy.mongo.database.subscribers.ExistSubscriber;
import org.waraccademy.mongo.database.subscribers.ObservableSubscriber;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ConnectorManager {
    private final Executor executor = (command) -> {
        Bukkit.getScheduler().runTaskAsynchronously(MongoPlugin.getInstance(), command);
    };

    private MongoDatabase database;

    public ConnectorManager(JavaPlugin plugin) {
        MongoClientSettings settings = this.getSettings(plugin.getConfig().getConfigurationSection("database"));
        this.initialize(settings, plugin.getConfig().getString("database.database"));
    }

    private void initialize(MongoClientSettings settings, String database) {
        MongoClient client = MongoClients.create(settings);
        this.database = client.getDatabase(database);
    }

    public <T> MongoCollection<T> getCollection(String name, Class<T> type) {
        return this.database.getCollection(name, type);
    }

    public MongoCollection<Document> getCollection(String name) {
        return this.getCollection(name, Document.class);
    }

    public CompletableFuture<Boolean> collectionExist(String name) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                Publisher<String> publisher = this.database.listCollectionNames();
                ExistSubscriber subscriber = new ExistSubscriber(name);
                publisher.subscribe(subscriber);

                try {
                    subscriber.await();
                } catch (Throwable var5) {
                    var5.printStackTrace();
                }

                return subscriber.isExist();
            }, this.executor);
        } catch (Throwable var3) {
            throw var3;
        }
    }

    public MongoCollection<Document> createCollectionAndGet(String name) {
        try {
            this.createCollection(name);
            return this.getCollection(name);
        } catch (Throwable var3) {
            throw var3;
        }
    }

    public void createCollection(String name) {
        this.createCollection(name, new ObservableSubscriber());
    }

    public void createCollection(String name, Subscriber<? super Void> subscriber) {
        this.collectionExist(name).whenComplete((exists, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }

            if (!exists) {
                Publisher<Void> publisher = this.database.createCollection(name);
                publisher.subscribe(subscriber);
            }

        });
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    private MongoClientSettings getSettings(ConfigurationSection section) {
        return (new MongoSettingsBuilder()).setHost(section.getString("host")).setPort(section.getInt("port")).setDatabase(section.getString("database")).setUsername(section.getString("authentication.user")).setPassword(section.getString("authentication.password")).build();
    }

}
