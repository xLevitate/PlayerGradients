package me.levitate.playergradients.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.data.DataWrapper;
import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import me.levitate.playergradients.inventory.GradientSelector;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "playergradients", aliases = {"gradient", "gradients", "playergradient", "pg"})
@Permission("pg.use")
public class GradientCommand {
    private final GradientManager gradientManager;
    private final Configuration config;
    private final DataWrapper dataWrapper;

    public GradientCommand(GradientManager gradientManager, Configuration config, DataWrapper dataWrapper) {
        this.gradientManager = gradientManager;
        this.config = config;
        this.dataWrapper = dataWrapper;
    }

    @Execute()
    public void onGradient(@Context CommandSender sender) {
        new GradientSelector(config, gradientManager).open((Player) sender);
    }

    @Execute(name = "reload")
    @Permission("pg.reload")
    public void onReload(@Context CommandSender sender) {
        dataWrapper.reloadGradients();
        config.reloadConfig();

        if (sender instanceof Player player)
            config.sendMessage(player, "reload");
    }

    @Execute(name = "create")
    @Permission("pg.create")
    public void onCreate(@Context CommandSender sender, @Arg("name") String name, @Arg String placeholder) {
        if (!placeholder.contains("%name%")) {
            if (sender instanceof Player player)
                config.sendMessage(player, "no-name");

            return;
        }

        if (gradientManager.getGradientByName(name) != null) {
            if (sender instanceof Player player)
                config.sendMessage(player, "already-exists");

            return;
        }

        final Gradient gradient = gradientManager.createGradient(name, placeholder);
        dataWrapper.addGradient(gradient);
    }

    @Execute(name = "delete", aliases = {"remove"})
    @Permission("pg.delete")
    public void onDelete(@Context CommandSender sender, @Arg Gradient gradient) {
        gradientManager.deleteGradient(gradient);
    }

    @Execute(name = "equip")
    public void onEquip(@Context Player player, @Arg Gradient gradient) {
        gradientManager.equipGradient(player, gradient);
    }

    @Execute(name = "unequip")
    public void onUnequip(@Context Player player) {
        final Gradient gradient = gradientManager.getGradient(player.getUniqueId());
        if (gradient == null) {
            config.sendMessage(player, "not-equipped");
            return;
        }

        gradientManager.unequipGradient(player);
    }
}
