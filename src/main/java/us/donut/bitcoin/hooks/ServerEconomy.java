package us.donut.bitcoin.hooks;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.donut.bitcoin.Bitcoin;
import us.donut.bitcoin.config.BitcoinConfig;

public class ServerEconomy {

    private static Bitcoin plugin;
    private static Economy economy;
    private static PlayerPointsAPI playerPointsAPI;
    private static boolean usePlayerPoints;

    public static void hook() {
        plugin = Bitcoin.getInstance();
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    public static void reload() {
        usePlayerPoints = BitcoinConfig.getBoolean("use_playerpoints");
        if (usePlayerPoints) {
            Plugin plugin = ServerEconomy.plugin.getServer().getPluginManager().getPlugin("PlayerPoints");
            playerPointsAPI = ((PlayerPoints) plugin).getAPI();
        }
    }

    public static boolean isPresent() {
        return economy != null || usePlayerPoints;
    }

    public static void deposit(OfflinePlayer player, String worldName, double amount) {
        if (usePlayerPoints) {
            playerPointsAPI.give(player.getUniqueId(), (int) amount);
        } else {
            economy.depositPlayer(player, worldName, amount);
        }
    }

    public static void withdraw(OfflinePlayer player, String worldName, double amount) {
        if (usePlayerPoints) {
            playerPointsAPI.take(player.getUniqueId(), (int) amount);
        } else {
            economy.withdrawPlayer(player, worldName, amount);
        }
    }

    public static double getBalance(OfflinePlayer player) {
        if (usePlayerPoints) {
            return playerPointsAPI.look(player.getUniqueId());
        } else {
            return economy.getBalance(player);
        }
    }
}
