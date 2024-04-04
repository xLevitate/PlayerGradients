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

import java.util.UUID;

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
    public void onCreate(@Context CommandSender sender, @Arg String name, @Arg String placeholder) {
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
    public void onDelete(@Context CommandSender sender, @Arg String name) {
        final Gradient gradient = gradientManager.getGradientByName(name);
        if (gradient == null) {
            if (sender instanceof Player player)
                config.sendMessage(player, "nonexistent");

            return;
        }

        gradientManager.deleteGradient(gradient);
    }

    @Execute(name = "equip")
    public void onEquip(@Context CommandSender sender, @Arg String name) {
        if (!(sender instanceof Player player))
            return;

        gradientManager.equipGradient(player, name);
    }

    @Execute(name = "unequip")
    public void onUnequip(@Context CommandSender sender) {
        if (!(sender instanceof Player player))
            return;

        final UUID playerUUID = player.getUniqueId();

        final Gradient gradient = gradientManager.getGradient(playerUUID);
        if (gradient == null) {
            config.sendMessage(player, "not-equipped");
            return;
        }

        gradientManager.unequipGradient(playerUUID);
    }

}
