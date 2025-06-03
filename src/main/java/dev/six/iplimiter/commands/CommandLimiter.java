package dev.six.iplimiter.commands;

import dev.six.iplimiter.configurations.ConfigurationManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLimiter extends Command implements TabExecutor {
    private final ConfigurationManager config;

    public CommandLimiter(ConfigurationManager config) {
        super("iplimiter", "iplimit.admin");
        this.config = config;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                config.reload();
                sender.sendMessage(new TextComponent(config.getMsgReloadSuccess()));
                break;

            case "setmax":
                if (args.length != 2) {
                    sender.sendMessage(new TextComponent(config.getMsgSetMaxUsage()));
                    return;
                }
                try {
                    int limit = Integer.parseInt(args[1]);
                    config.setMaxConnectionsPerIp(limit);
                    sender.sendMessage(new TextComponent(config.getMsgSetMaxSuccess(limit)));
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TextComponent(config.getMsgSetMaxNumberError()));
                }
                break;

            case "setmessage":
                if (args.length < 2) {
                    sender.sendMessage(new TextComponent(config.getMsgSetMessageUsage()));
                    return;
                }
                String message = String.join(" ", args).substring("setmessage".length()).trim();
                config.setKickMessage(message);
                sender.sendMessage(new TextComponent(config.getMsgSetMessageSuccess(message)));
                break;

            default:
                sendUsage(sender);
                break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("reload", "setmax", "setmessage");
            for (String cmd : subcommands) {
                if (cmd.startsWith(args[0].toLowerCase())) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2) {
            if ("setmax".equalsIgnoreCase(args[0])) {
                completions.add("1");
                completions.add("3");
                completions.add("5");
                completions.add("10");
            }
        }
        return completions;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(new TextComponent(config.getMsgUsageHeader()));
        sender.sendMessage(new TextComponent(config.getMsgUsageReload()));
        sender.sendMessage(new TextComponent(config.getMsgUsageSetMax()));
        sender.sendMessage(new TextComponent(config.getMsgUsageSetMessage()));
    }
}
