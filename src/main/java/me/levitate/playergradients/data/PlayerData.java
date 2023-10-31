package me.levitate.playergradients.data;

import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final GradientManager gradientManager;

    public PlayerData(JavaPlugin plugin, GradientManager gradientManager) {
        this.gradientManager = gradientManager;
        dataFile = new File(plugin.getDataFolder(), "players.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveEquippedGradients() {
        ConfigurationSection section = dataConfig.createSection("equippedGradients");
        for (Map.Entry<UUID, Gradient> entry : gradientManager.getEquippedGradient().entrySet()) {
            section.set(entry.getKey().toString(), entry.getValue().getName());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save player data.");
        }
    }

    public void checkEquippedGradients() {
        gradientManager.getEquippedGradient().entrySet().removeIf(entry ->
                !gradientManager.getGradientSet().contains(entry.getValue())
        );
    }

    public void loadEquippedGradients() {
        ConfigurationSection section = dataConfig.getConfigurationSection("equippedGradients");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID playerUUID = UUID.fromString(key);
                String gradientName = section.getString(key);
                Gradient gradient = gradientManager.getGradientByName(gradientName);
                if (gradient != null) {
                    gradientManager.equipGradient(playerUUID, gradient);
                }
            }
        }
    }
}
