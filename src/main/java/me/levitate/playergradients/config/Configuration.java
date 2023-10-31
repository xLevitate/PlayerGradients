package me.levitate.playergradients.config;

import lombok.Getter;

import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private final Plugin plugin;
    private FileConfiguration fileConfiguration;

    @Getter
    private final Map<String, String> messages = new LinkedHashMap<>();

    // Settings
    public String menuTitle = "";
    public boolean menuBorders = true;
    public String itemName = "";
    public List<String> loreList;

    public Configuration(Plugin plugin, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;

        reloadConfig();
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(messages.get(message)));
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveDefaultConfig();

        this.fileConfiguration = plugin.getConfig();
        fileConfiguration.options().copyDefaults(true);
        plugin.saveConfig();

        loadMessages();
        loadSettings();
    }

    private void loadMessages() {
        messages.clear();

        for (String key : fileConfiguration.getConfigurationSection("messages").getKeys(false)) {
            messages.put(key, fileConfiguration.getString("messages." + key));
        }
    }

    private void loadSettings() {
        menuTitle = fileConfiguration.getString("menu.menu-title");
        menuBorders = fileConfiguration.getBoolean("menu.menu-borders");
        itemName = fileConfiguration.getString("menu.item-name");
        loreList = fileConfiguration.getStringList("menu.item-lore");
    }
}
