package rkdgus.Listener;

import org.bukkit.event.Listener;
import rkdgus.AutoListen;

public abstract class craftBukkit implements Listener {
    protected craftBukkit() {
    }
    protected AutoListen getAutoListen() {
        return AutoListen.AutoIns();
    }
}
