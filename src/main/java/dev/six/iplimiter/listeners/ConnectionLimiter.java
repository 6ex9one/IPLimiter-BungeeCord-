package dev.six.iplimiter.listeners;

import dev.six.iplimiter.IPLimiter;
import dev.six.iplimiter.api.events.IPLimitEvent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class ConnectionLimiter implements Listener {

    private final IPLimiter plugin;

    public ConnectionLimiter(IPLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        String ip = extractIp(event.getConnection());
        if ("unknown".equals(ip)) {
            return;
        }

        int maxConnections = plugin.getConfigurationManager().getMaxConnectionsPerIp();
        long currentConnections = plugin.getProxy().getPlayers().stream()
                .filter(player -> ip.equals(extractIp(player)))
                .count();

        if (currentConnections >= maxConnections) {
            IPLimitEvent limitEvent = new IPLimitEvent(ip, maxConnections);
            plugin.getProxy().getPluginManager().callEvent(limitEvent);

            if (!limitEvent.isCancelled()) {
                String kickMessage = plugin.getConfigurationManager()
                        .getKickMessage(maxConnections);
                event.setCancelReason(kickMessage);
                event.setCancelled(true);
            }
        }
    }

    private String extractIp(PendingConnection connection) {
        InetSocketAddress address = connection.getAddress();
        return address != null ? address.getAddress().getHostAddress() : "unknown";
    }

    private String extractIp(ProxiedPlayer player) {
        InetSocketAddress address = (player.getSocketAddress() instanceof InetSocketAddress)
                ? (InetSocketAddress) player.getSocketAddress()
                : null;
        return address != null ? address.getAddress().getHostAddress() : "unknown";
    }
}
