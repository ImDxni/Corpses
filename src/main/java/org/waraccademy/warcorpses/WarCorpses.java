package org.waraccademy.warcorpses;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.waraccademy.warcorpses.commands.LogCommand;
import org.waraccademy.warcorpses.commands.RemoveCommand;
import org.waraccademy.warcorpses.configuration.FileManager;
import org.waraccademy.warcorpses.database.manager.ConnectorManager;
import org.waraccademy.warcorpses.database.manager.DatabaseManager;
import org.waraccademy.warcorpses.listener.DeathListener;
import org.waraccademy.warcorpses.traits.CorpseTrait;

public final class WarCorpses extends JavaPlugin {
    private static WarCorpses instance;
    private ConnectorManager mongo;
    private DatabaseManager manager;

    private YamlConfiguration config;
    @Override
    public void onEnable() {
        instance = this;

        FileManager fileManager = new FileManager("config",this);
        fileManager.saveDefault();
        config = fileManager.getConfig();

        mongo = new ConnectorManager(this);
        manager = new DatabaseManager();

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CorpseTrait.class));
        Bukkit.getPluginManager().registerEvents(new DeathListener(),this);

        getCommand("corpselog").setExecutor(new LogCommand());
        getCommand("removecorpse").setExecutor(new RemoveCommand());
    }

    public ConnectorManager getMongo() {
        return mongo;
    }

    public DatabaseManager getManager() {
        return manager;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public static WarCorpses getInstance() {
        return instance;
    }
}
