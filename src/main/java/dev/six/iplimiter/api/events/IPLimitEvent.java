package dev.six.iplimiter.api.events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class IPLimitEvent extends Event implements Cancellable {

    private final String ip;
    private final int limit;
    private boolean cancelled;

    public IPLimitEvent(String ip, int limit) {
        this.ip = ip;
        this.limit = limit;
        this.cancelled = false;
    }

    public String getIp() {
        return ip;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}