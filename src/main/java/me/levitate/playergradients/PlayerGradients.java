package me.levitate.playergradients;

import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import fr.mrmicky.fastinv.FastInvManager;
import me.levitate.playergradients.command.GradientCommand;
import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.data.DataWrapper;
import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import me.levitate.playergradients.papi.Placeholder;
import me.levitate.playergradients.resolver.GradientResolver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerGradients extends JavaPlugin {
    private DataWrapper dataWrapper;
    private Placeholder placeholder;

    @Override
    public void onEnable() {
        final Configuration config = new Configuration(this, getConfig());
        final GradientManager gradientManager = new GradientManager(config);

        dataWrapper = new DataWrapper(this, gradientManager);
        dataWrapper.loadAllData();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholder = new Placeholder(gradientManager);
            placeholder.register();
        }

        FastInvManager.register(this);

        LiteCommandsBukkit.builder("PlayerGradients", this)
                .commands(new GradientCommand(gradientManager, config, dataWrapper))
                .argument(Gradient.class, new GradientResolver(gradientManager))
                .build();
    }

    @Override
    public void onDisable() {
        dataWrapper.saveAllData();
        placeholder.unregister();
    }
}
