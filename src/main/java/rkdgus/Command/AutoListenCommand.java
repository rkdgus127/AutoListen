package rkdgus.Command;

import rkdgus.AutoListen;
import rkdgus.Listener.craftBukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class AutoListenCommand implements CommandExecutor, TabCompleter {
    private final AutoListen plugin;

    public AutoListenCommand(AutoListen plugin) {
        this.plugin = plugin;
    }

    private boolean isProtected(Plugin target) {
        String name = target.getName();

        if (target == plugin
                || name.equalsIgnoreCase("Konfig")
                || name.equalsIgnoreCase("AutoListen")) {
            return true;
        }

        PluginDescriptionFile description = target.getDescription();

        List<String> dependencies = new ArrayList<>();
        dependencies.addAll(description.getDepend());
        dependencies.addAll(description.getSoftDepend());
        dependencies.addAll(description.getLoadBefore());

        boolean dependsOnAutoListen = dependencies.stream()
                .anyMatch(dependency -> dependency.equalsIgnoreCase("AutoListen"));

        if (!dependsOnAutoListen) {
            return true;
        }

        return !hasCraftBukkitListener(target);
    }

    private boolean hasCraftBukkitListener(Plugin target) {
        try {
            for (String className : plugin.findClasses(target)) {
                Class<?> type = Class.forName(
                        className,
                        false,
                        target.getClass().getClassLoader()
                );

                if (type != craftBukkit.class
                        && craftBukkit.class.isAssignableFrom(type)) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }

        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§c사용법: /listen <start|stop|enable|disable|reload> <플러그인명>");
            return true;
        }

        String action = args[0].toLowerCase(Locale.ROOT);
        Plugin target = plugin.findPlugin(args[1]);

        if (target == null) {
            sender.sendMessage("§c해당 플러그인을 찾을 수 없습니다.");
            return true;
        }

        if (isProtected(target)) {
            sender.sendMessage("§c해당 플러그인은 listen 명령어로 제어할 수 없습니다.");
            return true;
        }

        try {
            switch (action) {
                case "start" -> {
                    if (!target.isEnabled()) {
                        sender.sendMessage("§c플러그인이 비활성화되어 있습니다.");
                    } else if (plugin.isRegistered(target)) {
                        sender.sendMessage("§e이미 리스너가 실행 중입니다.");
                    } else {
                        plugin.scan(target);
                        sender.sendMessage("§a" + target.getName() + " 리스너를 시작했습니다.");
                    }
                }

                case "stop" -> {
                    if (!plugin.isRegistered(target)) {
                        sender.sendMessage("§e이미 리스너가 중지되어 있습니다.");
                    } else {
                        plugin.unregister(target);
                        sender.sendMessage("§a" + target.getName() + " 리스너를 중지했습니다.");
                    }
                }

                case "enable" -> {
                    if (target.isEnabled()) {
                        sender.sendMessage("§e이미 활성화된 플러그인입니다.");
                    } else if (plugin.startPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 활성화했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 활성화에 실패했습니다.");
                    }
                }

                case "disable" -> {
                    if (!target.isEnabled()) {
                        sender.sendMessage("§e이미 비활성화된 플러그인입니다.");
                    } else if (plugin.stopPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 비활성화했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 비활성화에 실패했습니다.");
                    }
                }

                case "reload" -> {
                    if (plugin.reloadPlugin(target)) {
                        sender.sendMessage("§a" + target.getName() + " 플러그인을 리로드했습니다.");
                    } else {
                        sender.sendMessage("§c플러그인 리로드에 실패했습니다.");
                    }
                }

                default -> sender.sendMessage("§c사용법: /listen <start|stop|enable|disable|reload> <플러그인명>");
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
            for (String action : List.of("start", "stop", "enable", "disable", "reload")) {
                if (action.startsWith(current)) {
                    completions.add(action);
                }
            }
        } else if (args.length == 2) {
            for (Plugin target : plugin.getServer().getPluginManager().getPlugins()) {
                String name = target.getName();

                if (!isProtected(target)
                        && name.toLowerCase(Locale.ROOT).startsWith(current)) {
                    completions.add(name);
                }
            }
        }

        return completions;
    }
}