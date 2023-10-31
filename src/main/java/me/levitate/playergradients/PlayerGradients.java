package me.levitate.playergradients;

import co.aikar.commands.PaperCommandManager;
import fr.mrmicky.fastinv.FastInvManager;
import me.levitate.playergradients.command.MainCommand;
import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.data.DataWrapper;
import me.levitate.playergradients.gradients.GradientManager;
import me.levitate.playergradients.papi.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerGradients extends JavaPlugin {
    private DataWrapper dataWrapper;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        final Configuration config = new Configuration(this, getConfig());
        final GradientManager gradientManager = new GradientManager(config);

        dataWrapper = new DataWrapper(this, gradientManager);
        dataWrapper.loadAllData();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder(gradientManager).register();
        }

        FastInvManager.register(this);

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("gradientnames", c -> gradientManager.getGradientNames());
        commandManager.registerCommand(new MainCommand(gradientManager, config, dataWrapper));
    }

    @Override
    public void onDisable() {
        dataWrapper.saveAllData();
    }
}
