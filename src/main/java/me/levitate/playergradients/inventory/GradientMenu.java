package me.levitate.playergradients.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.gradients.GradientManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GradientMenu extends PaginatedGui {
    private final Player player;
    private final Configuration config;
    private final GradientManager gradientManager;

    public GradientMenu(Player player, Configuration config, GradientManager gradientManager) {
        super(6, 28, ChatColor.translateAlternateColorCodes('&', config.menuTitle), InteractionModifier.VALUES);

        this.player = player;
        this.config = config;
        this.gradientManager = gradientManager;

        this.drawMenu();
    }

    void drawMenu() {
        gradientManager.getGradientSet().forEach(gradient -> {
            if (player.hasPermission(gradient.getPermission())) {
                final GuiItem item = ItemBuilder.from(gradientManager.createGradientItem(player, gradient)).asGuiItem(e -> {
                    Player target = (Player) e.getWhoClicked();
                    gradientManager.equipGradient(target, gradient.getName());
                });

                addItem(item);
            }
        });
    }
}
