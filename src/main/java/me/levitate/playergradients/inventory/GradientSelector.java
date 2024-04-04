package me.levitate.playergradients.inventory;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;

import me.levitate.playergradients.config.Configuration;
import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import me.levitate.playergradients.utils.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class GradientSelector extends FastInv {
    private final Configuration config;
    private final GradientManager gradientManager;

    public GradientSelector(Configuration config, GradientManager gradientManager) {
        super(54, config.menuTitle);

        this.config = config;
        this.gradientManager = gradientManager;

        if (config.menuBorders) {
            setItems(getBorders(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();

        for (Gradient gradient : gradientManager.getGradientSet()) {
            if (!player.hasPermission(gradient.getPermission()))
                continue;

            final int slot = Utils.getNextAvailableSlot(event.getInventory());
            final ItemStack gradientItem = gradientManager.createGradientItem(player, gradient);

            setItem(slot, gradientItem, e -> {
                Player target = (Player) e.getWhoClicked();
                gradientManager.equipGradient(target, gradient.getName());
            });
        }
    }
}