# IPLimiter

IPLimiter is a simple yet powerful BungeeCord plugin that limits the number of simultaneous connections from a single IP address. It supports custom messages, allows ignoring limits for specific players, provides an easy-to-use API, and includes metrics via bStats.

---

## 🚀 Features

- 🔒 Limit the number of players connecting from the same IP
- 📃 Fully customizable messages (supports `\n` for multiline)
- 🛡️ Ignore limits for certain users
- 📦 Easy-to-use API (`IPLimitEvent`)
- 📊 bStats integration for metrics
- ✅ Lightweight and fast

---

## ⚙️ Installation

1. Download the JAR file and place it in your BungeeCord server's `plugins/` folder.
2. Restart the server.
3. Edit the `config.yml` as needed.

---

## 📁 Configuration (`config.yml`)

```yaml
max-connections-per-ip: 3
kick-message: "There are already &c%limit% &7players connected from this IP!"

commands-enabled: true
metrics-enabled: true

messages:
  reload-success: "&aConfiguration reloaded successfully."
  setmax-usage: "&cUsage: /iplimiter setmax <number>"
  setmax-success: "&aMax connections per IP set to: &e%limit%"
  setmessage-usage: "&cUsage: /iplimiter setmessage <message>"
  setmessage-success: "&aKick message updated to:\n&e%message%"
  setmax-number-error: "&cError: please enter a valid number."
  usage-header: "&7Usage of the /iplimiter command:"
  usage:
    reload: "&7/iplimiter reload &8- &fReload the configuration"
    setmax: "&7/iplimiter setmax <number> &8- &fChange max connections limit"
    setmessage: "&7/iplimiter setmessage <message> &8- &fChange the kick message"

```
## API

The plugin provides an event that allows other plugins to react to player connection attempts from an IP address and, if necessary, cancel those connections.

### Event `IPLimitEvent`

This event is fired when a player tries to connect and the connection limit for their IP address has been exceeded.

- **Methods:**
    - `Player getPlayer()` — Gets the player object who is trying to connect.
    - `String getIp()` — Gets the player's IP address.
    - `int getLimit()` — Gets the maximum allowed connections per IP.
    - `void setCancelled(boolean cancel)` — Cancels (or allows) the connection.
    - `boolean isCancelled()` — Checks whether the event is cancelled.

### Example of using the API to cancel a connection

```java
import dev.six.iplimiter.event.IPLimitEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;

public class MyListener implements Listener {

    @EventHandler
    public void onIPLimit(IPLimitEvent event) {
        // Cancel connection for a specific IP range
        if (event.getIp().startsWith("192.168.")) {
            event.setCancelled(true);
            ProxiedPlayer player = event.getPlayer();
            player.disconnect(new TextComponent("Connections from this IP are blocked."));
        }
    }
}
```

---

## About the Developer

This plugin was developed by **6ex9one** — a passionate Minecraft developer focused on creating efficient and user-friendly server plugins.

If you have any questions, suggestions, or want to contribute, feel free to reach out:

- GitHub: [https://github.com/6ex9one](https://github.com/6ex9one)
- Discord: 6ex9one_
