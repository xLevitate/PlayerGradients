package me.levitate.playergradients.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.AllArgsConstructor;
import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.data.DataWrapper;
import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import me.levitate.playergradients.inventory.GradientSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("playergradient|gradients|pg|playergradients|gradient")
public class MainCommand extends BaseCommand {
    private final GradientManager gradientManager;
    private final Configuration config;
    private final DataWrapper dataWrapper;

    public MainCommand(GradientManager gradientManager, Configuration config, DataWrapper dataWrapper) {
        this.gradientManager = gradientManager;
        this.config = config;
        this.dataWrapper = dataWrapper;
    }

    @Default
    public void onGradient(CommandSender sender) {
        new GradientSelector(config, gradientManager).open((Player) sender);
    }

    @Subcommand("reload")
    @CommandPermission("pg.reload")
    public void onReload(CommandSender sender) {
        dataWrapper.reloadGradients();
        config.reloadConfig();

        config.sendMessage((Player) sender, "reload");
    }

    @Subcommand("create")
    @Syntax("<name> <placeholder>")
    @CommandPermission("pg.create")
    public void onCreate(CommandSender sender, String name, String placeholder) {
        Player player = (Player) sender;

        if (!placeholder.contains("%name%")) {
            config.sendMessage(player, "no-name");
            return;
        }

        if (gradientManager.getGradientByName(name) != null) {
            config.sendMessage(player, "already-exists");
            return;
        }

        Gradient gradient = gradientManager.createGradient(name, placeholder);
        dataWrapper.addGradient(gradient);
    }

    @Subcommand("delete")
    @Syntax("<name>")
    @CommandCompletion("@gradientnames")
    @CommandPermission("pg.delete")
    public void onDelete(CommandSender sender, String name) {
        Gradient gradient = gradientManager.getGradientByName(name);
        if (gradient == null) {
            config.sendMessage((Player) sender, "nonexistent");
            return;
        }

        gradientManager.deleteGradient(gradient);
    }

    @Subcommand("equip")
    @Syntax("<gradient>")
    @CommandCompletion("@gradientnames")
    public void onEquip(CommandSender sender, String name) {
        gradientManager.equipGradient((Player) sender, name);
    }

    @Subcommand("unequip")
    public void onUnequip(CommandSender sender) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        Gradient gradient = gradientManager.getGradient(playerUUID);
        if (gradient == null) {
            config.sendMessage((Player) sender, "not-equipped");
            return;
        }

        gradientManager.unequipGradient(playerUUID);
    }
}
