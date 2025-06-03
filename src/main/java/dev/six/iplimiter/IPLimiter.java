package dev.six.iplimiter;

import dev.six.iplimiter.bstats.Metrics;
import dev.six.iplimiter.commands.CommandLimiter;
import dev.six.iplimiter.configurations.ConfigurationManager;
import dev.six.iplimiter.listeners.ConnectionLimiter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class IPLimiter extends Plugin {

    private ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        this.configurationManager = new ConfigurationManager(this);

        PluginManager manager = getProxy().getPluginManager();
        manager.registerListener(this, new ConnectionLimiter(this));
        if (configurationManager.isCommandsEnabled()) {
            manager.registerCommand(this, new CommandLimiter(configurationManager));
        }
        if (configurationManager.isMetricsEnabled()) {
            new Metrics(this, 26072);
        }
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
}