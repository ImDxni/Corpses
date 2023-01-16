package org.waraccademy.warcorpses.database.object;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CorpseLog implements Log{
    private String player;
    private List<ItemLog> items = new ArrayList<>();
    private Date date = new Date();

    public CorpseLog(String player, List<ItemLog> items) {
        this.player = player;
        this.items = items;
    }

    public CorpseLog() {}

    @Override
    public Document getDocument() {
        Document document = new Document();

        document.put("player",player);
        document.put("date",date);

        List<Document> documents = items.stream()
                .map(ItemLog::getDocument)
                .collect(Collectors.toList());

        document.put("items",documents);


        return document;
    }

    public static CorpseLog of(Document document){
        CorpseLog corpseLog = new CorpseLog();

        corpseLog.setPlayer(document.getString("player"));
        corpseLog.setDate(document.getDate("date"));
        corpseLog.setItems(document.getList("items",Document.class)
                .stream()
                .map(ItemLog::of)
                .collect(Collectors.toList()));

        return corpseLog;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<ItemLog> getItems() {
        return items;
    }

    public void setItems(List<ItemLog> items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addItemLog(ItemLog  item) {
        items.add(item);
    }
}
