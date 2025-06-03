# IPLimiter

IPLimiter — это простой, но мощный плагин для BungeeCord, который ограничивает количество одновременных подключений с одного IP-адреса. Поддерживает кастомные сообщения, позволяет игнорировать лимиты для определённых игроков, предоставляет простой в использовании API и включает метрики через bStats.

---

## 🚀 Возможности

- 🔒 Ограничение количества игроков, подключающихся с одного IP
- 📃 Полная настройка сообщений (поддержка `\n` для многострочного текста)
- 🛡️ Игнорирование лимита для определённых пользователей
- 📦 Простой в использовании API (`IPLimitEvent`)
- 📊 Интеграция с bStats для сбора метрик
- ✅ Лёгкий и быстрый

---

## ⚙️ Установка

1. Скачайте JAR-файл и поместите его в папку `plugins/` вашего BungeeCord-сервера.
2. Перезапустите сервер.
3. Отредактируйте файл `config.yml` по необходимости.

---

## 📁 Конфигурация (`config.yml`)

```yaml
max-connections-per-ip: 3
kick-message: "There are already &c%limit% &7players connected from this IP!"

commands-enabled: true
metrics-enabled: true

messages:
  reload-success: "&aКонфигурация успешно перезагружена."
  setmax-usage: "&cИспользование: /iplimiter setmax <число>"
  setmax-success: "&aМаксимум подключений на IP установлен: &e%limit%"
  setmessage-usage: "&cИспользование: /iplimiter setmessage <сообщение>"
  setmessage-success: "&aСообщение при кике обновлено на:\n&e%message%"
  setmax-number-error: "&cОшибка: введите корректное число."
  usage-header: "&7Использование команды /iplimiter:"
  usage:
    reload: "&7/iplimiter reload &8- &fПерезагрузить конфигурацию"
    setmax: "&7/iplimiter setmax <число> &8- &fИзменить лимит подключений"
    setmessage: "&7/iplimiter setmessage <сообщение> &8- &fИзменить сообщение кика"
```

## API

Плагин предоставляет событие, позволяющее другим плагинам реагировать на попытки подключения игроков с IP-адреса и при необходимости отменять эти подключения.

### Событие `IPLimitEvent`

Это событие вызывается, когда игрок пытается подключиться, а лимит подключений с его IP превышен.

- **Методы:**
    - `Player getPlayer()` — получить объект игрока, который пытается подключиться.
    - `String getIp()` — получить IP-адрес игрока.
    - `int getLimit()` — получить максимальный лимит подключений на IP.
    - `void setCancelled(boolean cancel)` — отменить (или разрешить) подключение.
    - `boolean isCancelled()` — проверить, отменено ли событие.

### Пример использования API для отмены подключения

```java
import dev.six.iplimiter.event.IPLimitEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;

public class MyListener implements Listener {

    @EventHandler
    public void onIPLimit(IPLimitEvent event) {
        // Отмена подключения для определённого диапазона IP
        if (event.getIp().startsWith("192.168.")) {
            event.setCancelled(true);
            ProxiedPlayer player = event.getPlayer();
            player.disconnect(new TextComponent("Подключения с этого IP запрещены."));
        }
    }
}
```

## Об авторе

Плагин разработан **6ex9one** — увлечённым разработчиком Minecraft, который создаёт эффективные и удобные плагины для серверов.

Если у вас есть вопросы, предложения или вы хотите помочь с разработкой, вы можете связаться со мной:

- GitHub: [https://github.com/6ex9one](https://github.com/6ex9one)
- Discord: 6ex9one_
