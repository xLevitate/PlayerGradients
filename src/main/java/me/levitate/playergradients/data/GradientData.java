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

public class GradientData {
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final GradientManager gradientManager;

    public GradientData(JavaPlugin plugin, GradientManager gradientManager) {
        this.gradientManager = gradientManager;

        dataFile = new File(plugin.getDataFolder(), "gradients.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        if (!dataFile.exists()) {
            try {
                dataConfig.set("gradients." + "default" + ".placeholder", "<gradient:green:blue>%name%</gradient>");
                dataConfig.save(dataFile);
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to save gradient data.");
            }
        }

        loadGradients();
    }

    public void saveGradients() {
        for (Gradient gradient : gradientManager.getGradientSet()) {
            dataConfig.set("gradients." + gradient.getName() + ".placeholder", gradient.getPlaceholder());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save gradient data.");
        }
    }

    public void addGradient(Gradient gradient) {
        dataConfig.set("gradients." + gradient.getName() + ".placeholder", gradient.getPlaceholder());

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save gradient data.");
        }
    }

    public void loadGradients() {
        gradientManager.getGradientSet().clear();

        final ConfigurationSection gradientsSection = dataConfig.getConfigurationSection("gradients");
        if (gradientsSection == null)
            return;

        for (String key : gradientsSection.getKeys(false)) {
            final String placeholder = gradientsSection.getString(key + ".placeholder");
            gradientManager.createGradient(key, placeholder);
        }
    }

    public void reloadGradients() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadGradients();
    }
}