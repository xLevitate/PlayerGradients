package me.levitate.playergradients.papi;

import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.levitate.playergradients.gradients.GradientManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Placeholder extends PlaceholderExpansion {
    private final GradientManager gradientManager;

    @Override
    public @NotNull String getIdentifier() {
        return "pg";
    }

    @Override
    public @NotNull String getAuthor() {
        return "levitate";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if(params.equalsIgnoreCase("name")) {
            if (offlinePlayer != null) {
                Player player = (Player) offlinePlayer;

                return gradientManager.convertGradient(player);
            }
        }

        return null;
    }
}