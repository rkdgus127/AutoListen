package rkdgus.Command;

import rkdgus.AutoListen;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AutoListenCommand implements CommandExecutor, TabCompleter {
    private final AutoListen plugin;

    public AutoListenCommand(AutoListen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§c사용법: /listen <start|stop|reload> <플러그인명>");
            return true;
        }

        String action = args[0].toLowerCase(Locale.ROOT);
        Plugin target = plugin.findPlugin(args[1]);

        if (target == null) {
            sender.sendMessage("§c해당 플러그인을 찾을 수 없습니다.");
            return true;
        }

        if (target == plugin) {
            sender.sendMessage("§cAutoListen 자체는 제어할 수 없습니다.");
            return true;
        }

        try {
            switch (action) {
                case "start" -> {
                    if (target.isEnabled()) {
                        sender.sendMessage("§e이미 실행 중인 플러그인입니다.");
                    } else if (plugin.startPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 시작했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 시작에 실패했습니다.");
                    }
                }
                case "stop" -> {
                    if (!target.isEnabled()) {
                        sender.sendMessage("§e이미 종료된 플러그인입니다.");
                    } else if (plugin.stopPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 종료했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 종료에 실패했습니다.");
                    }
                }
                case "reload" -> {
                    if (plugin.reloadPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 리로드했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 리로드에 실패했습니다.");
                    }
                }
                default -> sender.sendMessage("§c사용법: /listen <start|stop|reload> <플러그인명>");
            }
        } catch (Throwable exception) {
            sender.sendMessage("§c플러그인 처리 중 오류가 발생했습니다: " + exception.getClass().getSimpleName());
            plugin.getLogger().severe(target.getName() + " 처리 실패: " + exception.getMessage());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        String current = args[args.length - 1].toLowerCase(Locale.ROOT);

        if (args.length == 1) {
            for (String action : List.of("start", "stop", "reload")) {
                if (action.startsWith(current)) {
                    completions.add(action);
                }
            }
        } else if (args.length == 2) {
            for (Plugin target : plugin.getServer().getPluginManager().getPlugins()) {
                String name = target.getName();
                if (target != plugin && name.toLowerCase(Locale.ROOT).startsWith(current)) {
                    completions.add(name);
                }
            }
        }

        return completions;
    }
}
