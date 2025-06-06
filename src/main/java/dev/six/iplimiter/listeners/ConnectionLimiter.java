package dev.six.iplimiter.listeners;

import dev.six.iplimiter.IPLimiter;
import dev.six.iplimiter.api.events.IPLimitEvent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionLimiter implements Listener {

    private final IPLimiter plugin;
    private final Map<String, AtomicInteger> ipConnectionCounts = new ConcurrentHashMap<>();

    public ConnectionLimiter(IPLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        String ip = extractIp(event.getConnection());
        if ("unknown".equals(ip)) return;

        int maxConnections = plugin.getConfigurationManager().getMaxConnectionsPerIp();
        int currentConnections = ipConnectionCounts.getOrDefault(ip, new AtomicInteger(0)).get();

        if (currentConnections >= maxConnections) {
            IPLimitEvent limitEvent = new IPLimitEvent(ip, maxConnections);
            plugin.getProxy().getPluginManager().callEvent(limitEvent);

            if (!limitEvent.isCancelled()) {
                String kickMessage = plugin.getConfigurationManager().getKickMessage(maxConnections);
                event.setCancelReason(kickMessage);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        String ip = extractIp(event.getPlayer());
        if ("unknown".equals(ip)) return;

        ipConnectionCounts.computeIfAbsent(ip, key -> new AtomicInteger(0)).incrementAndGet();
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        String ip = extractIp(event.getPlayer());
        if ("unknown".equals(ip)) return;

        AtomicInteger counter = ipConnectionCounts.get(ip);
        if (counter != null) {
            if (counter.decrementAndGet() <= 0) {
                ipConnectionCounts.remove(ip);
            }
        }
    }

    private String extractIp(PendingConnection connection) {
        InetSocketAddress address = connection.getAddress();
        return address != null ? address.getAddress().getHostAddress() : "unknown";
    }

    private String extractIp(ProxiedPlayer player) {
        SocketAddress socketAddress = player.getSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) socketAddress;
            return addr.getAddress().getHostAddress();
        }
        return "unknown";
    }
}

