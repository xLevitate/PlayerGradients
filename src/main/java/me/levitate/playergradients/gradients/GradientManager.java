package me.levitate.playergradients.gradients;

import com.google.common.collect.ImmutableList;

import lombok.Getter;

import me.levitate.playergradients.config.Configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
public class GradientManager {
    // Set to store gradients
    private final Set<Gradient> gradientSet = new HashSet<>();

    // Mapping of players to equipped gradients
    private final HashMap<UUID, Gradient> equippedGradient = new HashMap<>();

    private final Configuration config;

    public GradientManager(Configuration config) {
        this.config = config;
    }

    /**
     * Creates a new gradient with the given name and placeholder.
     *
     * @param name       The name of the gradient.
     * @param placeholder The placeholder of the gradient.
     * @return The created gradient.
     */
    public Gradient createGradient(String name, String placeholder) {
        final Gradient gradient = new Gradient(name, placeholder);
        gradientSet.add(gradient);

        return gradient;
    }

    /**
     * Deletes the specified gradient.
     *
     * @param gradient The gradient to be deleted.
     */
    public void deleteGradient(Gradient gradient) {
        gradientSet.remove(gradient);

        // Remove equipped gradient if anyone has it equipped.
        equippedGradient.entrySet().removeIf(entry -> entry.getValue().equals(gradient));
    }

    /**
     * Equips a gradient for a player.
     *
     * @param playerUUID The UUID of the player.
     * @param gradient   The gradient to be equipped.
     */
    public void equipGradient(UUID playerUUID, Gradient gradient) {
        equippedGradient.put(playerUUID, gradient);
    }

    /**
     * Equips a gradient for a player using the gradient name.
     *
     * @param player       The player to equip the gradient for.
     * @param gradientName The name of the gradient to equip.
     */
    public void equipGradient(Player player, String gradientName) {
        UUID playerUUID = player.getUniqueId();

        Gradient gradient = getGradientByName(gradientName);
        if (gradient == null) {
            config.sendMessage(player, "nonexistent");
            return;
        }

        if (!player.hasPermission(gradient.getPermission())) {
            config.sendMessage(player, "no-permission");
            return;
        }

        this.equipGradient(playerUUID, gradient);

        String placeholder = gradient.getPlaceholder().replaceAll("%name%", player.getName());
        player.sendMessage(MiniMessage.miniMessage().deserialize(config.getMessages().get("equipped").replaceAll("%placeholder%", placeholder)));
    }

    /**
     * Unequips a gradient from a player.
     *
     * @param playerUUID The UUID of the player.
     */
    public void unequipGradient(UUID playerUUID) {
        equippedGradient.remove(playerUUID);
    }

    /**
     * Retrieves a gradient using its name.
     *
     * @param name The name of the gradient.
     * @return The gradient with the specified name, or null if not found.
     */
    public Gradient getGradientByName(String name) {
        return gradientSet.stream()
                .filter(gradient -> gradient.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the equipped gradient for a player.
     *
     * @param playerUUID The UUID of the player.
     * @return The equipped gradient for the player, or null if none is equipped.
     */
    public Gradient getGradient(UUID playerUUID) {
        return equippedGradient.get(playerUUID);
    }

    /**
     * Converts a player's name to a formatted name with the equipped gradient.
     *
     * @param player The player whose name is to be converted.
     * @return The formatted name.
     */
    public String convertGradient(Player player) {
        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();

        final Gradient gradient = equippedGradient.get(playerUUID);
        if (gradient == null) {
            return playerName;
        }

        final String placeholder = gradient.getPlaceholder().replace("%name%", playerName);
        final Component gradientComponent = MiniMessage.miniMessage().deserialize(placeholder);

        return LegacyComponentSerializer.legacyAmpersand().serialize(gradientComponent);
    }

    /**
     * Retrieves a list of names of all gradients.
     *
     * @return An immutable list of gradient names.
     */
    public ImmutableList<String> getGradientNames() {
        List<String> gradientNames = gradientSet.stream()
                .map(Gradient::getName)
                .toList();

        return ImmutableList.copyOf(gradientNames);
    }

    /**
     * Creates an ItemStack representing a gradient item for a player.
     *
     * @param player   The player for whom the gradient item is being created.
     * @param gradient The gradient associated with the item.
     * @return The created ItemStack representing the gradient item.
     */
    public ItemStack createGradientItem(Player player, Gradient gradient) {
        ItemStack gradientItem = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = gradientItem.getItemMeta();

        String placeholder = gradient.getPlaceholder().replaceAll("%name%", player.getName());
        String configName = config.itemName.replaceAll("%placeholder%", placeholder);

        Component name = MiniMessage.miniMessage().deserialize(configName)
                .decoration(TextDecoration.ITALIC, false);
        itemMeta.displayName(name);

        List<Component> translatedLore = config.loreList.stream()
                .map(line -> MiniMessage.miniMessage().deserialize(line.replaceAll("%placeholder%", placeholder)).decoration(TextDecoration.ITALIC, false))
                .toList();
        itemMeta.lore(translatedLore);

        gradientItem.setItemMeta(itemMeta);

        return gradientItem;
    }
}
