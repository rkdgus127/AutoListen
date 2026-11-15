package rkdgus.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class ListenerRegisterEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Plugin plugin;
    private final Listener listener;

    public ListenerRegisterEvent(Plugin plugin, Listener listener) {
        this.plugin = plugin;
        this.listener = listener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}