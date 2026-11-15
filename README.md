<div align="center">

# AutoListen

<p align="center">

[![Minecraft](https://img.shields.io/badge/Minecraft-26.3-brightgreen?logo=microsoft)](https://www.minecraft.net/)
[![Platform](https://img.shields.io/badge/Platform-Paper-blue?logo=papermc)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-27-ED8B00.svg?logo=openjdk)](https://www.azul.com/)
[![License](https://img.shields.io/badge/License-GPL--3.0-blue?logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.html)

</p>

---


`build.gradle.kts`

```kotlin
dependencies {
    compileOnly(files("libs/AutoListen.jar"))
}
```

`plugin.yml`

```yaml
depend: [AutoListen]
```

기존 Bukkit의 `Listener` 대신 `craftBukkit`을 상속합니다.

```java
package example.Event;

import rkdgus.Listener.craftBukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends craftBukkit { //Kotlin 호환 

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("서버에 오신 것을 환영합니다!");
    }
}
```

기존 Bukkit의 `getInstance()` 대신 `AutoIns`를 사용합니다.

```java
package example.Rkdgus;

public class Test extends craftBukkit {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void ontest(PlayerMoveEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
            //예시 문장...
            };
        }
    }.runTaskLater(AutoListen.AutoIns(), 1L); //Kotlin 호환
}

```

## Command

|Command|description|
|---|---|
| `/listen start <플러그인명>` | 지정한 플러그인 리스너 활성화 |
| `/listen stop <플러그인명>` | 지정한 플러그인 리스너 비활성화 |
| `/listen reload <플러그인명>` | 지정한 플러그인 비활성화 후 다시 활성화 |
| `/listen enable <플러그인명>` | 지정한 플러그인 활성화 |
| `/listen disable <플러그인명>` | 지정한 플러그인 비활성화 |

---

<div align="center">

### AutoListen

</div>

@Copyright by rkdgus127
