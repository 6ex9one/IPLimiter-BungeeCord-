package dev.six.iplimiter.configurations;

import dev.six.iplimiter.IPLimiter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;

public class ConfigurationManager {

    private final IPLimiter plugin;

    private int maxConnectionsPerIp = 3;
    private String kickMessage = "There are already &c%limit% &7players connected from this IP!";

    private String msgReloadSuccess = "&aConfiguration reloaded successfully.";
    private String msgSetMaxUsage = "&cUsage: /iplimiter setmax <number>";
    private String msgSetMaxSuccess = "&aMax connections per IP set to: &e%limit%";
    private String msgSetMessageUsage = "&cUsage: /iplimiter setmessage <message>";
    private String msgSetMessageSuccess = "&aKick message updated to:\n&e%message%";
    private String msgSetMaxNumberError = "&cError: please enter a valid number.";
    private String msgUsageHeader = "&7Usage of the /iplimiter command:";
    private String msgUsageReload = "&7/iplimiter reload &8- &fReload the configuration";
    private String msgUsageSetMax = "&7/iplimiter setmax <number> &8- &fChange max connections limit";
    private String msgUsageSetMessage = "&7/iplimiter setmessage <message> &8- &fChange the kick message";

    private boolean commandsEnabled = true;
    private boolean metricsEnabled = true;

    public ConfigurationManager(IPLimiter plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                Configuration config = new Configuration();

                config.set("max-connections-per-ip", maxConnectionsPerIp);
                config.set("kick-message", kickMessage);

                config.set("commands-enabled", true);
                config.set("metrics-enabled", true);

                config.set("messages.reload-success", msgReloadSuccess);
                config.set("messages.setmax-usage", msgSetMaxUsage);
                config.set("messages.setmax-success", msgSetMaxSuccess);
                config.set("messages.setmessage-usage", msgSetMessageUsage);
                config.set("messages.setmessage-success", msgSetMessageSuccess);
                config.set("messages.setmax-number-error", msgSetMaxNumberError);
                config.set("messages.usage-header", msgUsageHeader);
                config.set("messages.usage.reload", msgUsageReload);
                config.set("messages.usage.setmax", msgUsageSetMax);
                config.set("messages.usage.setmessage", msgUsageSetMessage);

                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create config.yml: " + e.getMessage());
            }
        }

        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            this.maxConnectionsPerIp = config.getInt("max-connections-per-ip", maxConnectionsPerIp);
            this.kickMessage = config.getString("kick-message", kickMessage);

            this.commandsEnabled = config.getBoolean("commands-enabled", true);
            this.metricsEnabled = config.getBoolean("metrics-enabled", true);

            this.msgReloadSuccess = config.getString("messages.reload-success", msgReloadSuccess);
            this.msgSetMaxUsage = config.getString("messages.setmax-usage", msgSetMaxUsage);
            this.msgSetMaxSuccess = config.getString("messages.setmax-success", msgSetMaxSuccess);
            this.msgSetMessageUsage = config.getString("messages.setmessage-usage", msgSetMessageUsage);
            this.msgSetMessageSuccess = config.getString("messages.setmessage-success", msgSetMessageSuccess);
            this.msgSetMaxNumberError = config.getString("messages.setmax-number-error", msgSetMaxNumberError);
            this.msgUsageHeader = config.getString("messages.usage-header", msgUsageHeader);
            this.msgUsageReload = config.getString("messages.usage.reload", msgUsageReload);
            this.msgUsageSetMax = config.getString("messages.usage.setmax", msgUsageSetMax);
            this.msgUsageSetMessage = config.getString("messages.usage.setmessage", msgUsageSetMessage);

            plugin.getLogger().info("Configuration loaded successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load config.yml: " + e.getMessage());
        }
    }

    public int getMaxConnectionsPerIp() {
        return maxConnectionsPerIp;
    }

    public String getKickMessage(int limit) {
        return colorize(kickMessage.replace("%limit%", String.valueOf(limit)));
    }

    public boolean isCommandsEnabled() {
        return commandsEnabled;
    }

    public String getMsgReloadSuccess() {
        return colorize(msgReloadSuccess);
    }

    public String getMsgSetMaxUsage() {
        return colorize(msgSetMaxUsage);
    }

    public String getMsgSetMaxSuccess(int limit) {
        return colorize(msgSetMaxSuccess.replace("%limit%", String.valueOf(limit)));
    }

    public String getMsgSetMessageUsage() {
        return colorize(msgSetMessageUsage);
    }

    public String getMsgSetMessageSuccess(String message) {
        return colorize(msgSetMessageSuccess.replace("%message%", message));
    }

    public String getMsgSetMaxNumberError() {
        return colorize(msgSetMaxNumberError);
    }

    public String getMsgUsageHeader() {
        return colorize(msgUsageHeader);
    }

    public String getMsgUsageReload() {
        return colorize(msgUsageReload);
    }

    public String getMsgUsageSetMax() {
        return colorize(msgUsageSetMax);
    }

    public String getMsgUsageSetMessage() {
        return colorize(msgUsageSetMessage);
    }

    public void setMaxConnectionsPerIp(int maxConnectionsPerIp) {
        this.maxConnectionsPerIp = maxConnectionsPerIp;
        saveConfig();
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
        saveConfig();
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    private void saveConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            config.set("max-connections-per-ip", maxConnectionsPerIp);
            config.set("kick-message", kickMessage);

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config.yml: " + e.getMessage());
        }
    }

    private String colorize(String message) {
        return message == null ? null : ChatColor.translateAlternateColorCodes('&', message.replace("\\n", "\n"));
    }
}