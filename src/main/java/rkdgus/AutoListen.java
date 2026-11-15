package rkdgus;

import org.bukkit.event.EventHandler;
import rkdgus.Command.AutoListenCommand;
import rkdgus.Event.ListenerRegisterEvent;
import rkdgus.Event.ListenerUnregisterEvent;
import rkdgus.Listener.craftBukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public final class AutoListen extends JavaPlugin implements Listener {
    private final Map<Plugin, List<Listener>> registered = new IdentityHashMap<>();
    private final Set<Plugin> scanned = Collections.newSetFromMap(new IdentityHashMap<>());

    private static AutoListen instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(this, this);

        AutoListenCommand command = new AutoListenCommand(this);
        getCommand("listen").setExecutor(command);
        getCommand("listen").setTabCompleter(command);
    }

    @Override
    public void onDisable() {

        instance = null;
        for (List<Listener> listeners : registered.values()) {
            for (Listener listener : listeners) {
                HandlerList.unregisterAll(listener);
            }
        }
        registered.clear();
        scanned.clear();
        HandlerList.unregisterAll((Plugin) this);
    }

    public static AutoListen AutoIns() {
        return instance;
    }


    @org.bukkit.event.EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        if (plugin != this) {
            unregister(plugin);
        }
    }

    public boolean isRegistered(Plugin plugin) {
        return registered.containsKey(plugin);
    }

    public void scan(Plugin plugin) {
        if (!plugin.isEnabled() || scanned.contains(plugin)) {
            return;
        }

        scanned.add(plugin);
        List<Listener> listeners = new ArrayList<>();

        try {
            for (String className : findClasses(plugin)) {
                try {
                    Class<?> type = Class.forName(className, false, plugin.getClass().getClassLoader());

                    if (type == craftBukkit.class
                            || !craftBukkit.class.isAssignableFrom(type)
                            || type.isInterface()
                            || Modifier.isAbstract(type.getModifiers())) {
                        continue;
                    }

                    Constructor<?> constructor = type.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance();

                    if (!(instance instanceof Listener listener)) {
                        continue;
                    }

                    getServer().getPluginManager().registerEvents(listener, this);
                    listeners.add(listener);
                    getServer().getPluginManager().callEvent(new ListenerRegisterEvent(plugin, listener));
                } catch (NoSuchMethodException ignored) {
                } catch (Throwable exception) {
                    getLogger().warning("리스너 등록 실패: " + plugin.getName() + " / " + className + " / " + exception.getClass().getSimpleName());
                }
            }
        } catch (Throwable exception) {
            getLogger().warning("플러그인 클래스 탐색 실패: " + plugin.getName() + " / " + exception.getMessage());
        }

        registered.put(plugin, listeners);
        getLogger().info(plugin.getName() + " 자동 리스너 등록 완료: " + listeners.size() + "개");
    }

    private List<String> findClasses(Plugin plugin) throws IOException, URISyntaxException {
        Path location = Path.of(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        List<String> classes = new ArrayList<>();

        if (Files.isDirectory(location)) {
            try (Stream<Path> paths = Files.walk(location)) {
                paths.filter(path -> path.toString().endsWith(".class"))
                        .map(location::relativize)
                        .map(Path::toString)
                        .map(path -> path.replace('\\', '.').replace('/', '.'))
                        .map(path -> path.substring(0, path.length() - 6))
                        .filter(this::isLoadableClassName)
                        .forEach(classes::add);
            }
            return classes;
        }

        try (JarFile jar = new JarFile(location.toFile())) {
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!entry.isDirectory() && name.endsWith(".class") && !name.startsWith("META-INF/versions/")) {
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    if (isLoadableClassName(className)) {
                        classes.add(className);
                    }
                }
            }
        }

        return classes;
    }

    private boolean isLoadableClassName(String className) {
        return !className.equals("module-info")
                && !className.endsWith(".module-info")
                && !className.equals("package-info")
                && !className.endsWith(".package-info");
    }

    public void unregister(Plugin plugin) {
        scanned.remove(plugin);

        List<Listener> listeners = registered.remove(plugin);
        if (listeners == null) {
            return;
        }

        for (Listener listener : listeners) {
            getServer().getPluginManager().callEvent(new ListenerUnregisterEvent(plugin, listener));
            HandlerList.unregisterAll(listener);
        }
    }

    public List<Listener> getRegistered(Plugin plugin) {
        List<Listener> listeners = registered.get(plugin);
        return listeners == null ? List.of() : List.copyOf(listeners);
    }

    public Plugin findPlugin(String name) {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(name)) {
                return plugin;
            }
        }
        return null;
    }

    public boolean startPlugin(Plugin plugin) {
        if (plugin == this || plugin.isEnabled()) {
            return false;
        }
        getServer().getPluginManager().enablePlugin(plugin);
        return plugin.isEnabled();
    }

    public boolean stopPlugin(Plugin plugin) {
        if (plugin == this || !plugin.isEnabled()) {
            return false;
        }
        getServer().getPluginManager().disablePlugin(plugin);
        return !plugin.isEnabled();
    }

    public boolean reloadPlugin(Plugin plugin) {
        if (plugin == this) {
            return false;
        }
        if (plugin.isEnabled()) {
            getServer().getPluginManager().disablePlugin(plugin);
        }
        if (!plugin.isEnabled()) {
            getServer().getPluginManager().enablePlugin(plugin);
        }
        return plugin.isEnabled();
    }
}
