package me.levitate.playergradients.data;

import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DataWrapper {
    private final PlayerData playerData;
    private final GradientData gradientData;

    public DataWrapper(JavaPlugin plugin, GradientManager gradientManager) {
        playerData = new PlayerData(plugin, gradientManager);
        gradientData = new GradientData(plugin, gradientManager);

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllData, 2400L, 2400L);
    }

    public void saveAllData() {
        playerData.saveEquippedGradients();
        gradientData.saveGradients();
    }

    public void loadAllData() {
        playerData.loadEquippedGradients();
        gradientData.loadGradients();
    }

    public void reloadGradients() {
        gradientData.reloadGradients();
        playerData.checkEquippedGradients();
    }

    public void addGradient(Gradient gradient) {
        gradientData.addGradient(gradient);
    }
}