<div align="center">

# AutoListen

<p align="center">

[![Minecraft](https://img.shields.io/badge/Minecraft-26.3-brightgreen?logo=microsoft)](https://www.minecraft.net/)
[![Platform](https://img.shields.io/badge/Platform-Paper-blue?logo=papermc)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-27-ED8B00.svg?logo=openjdk)](https://www.azul.com/)
[![License](https://img.shields.io/badge/License-GPL--3.0-blue?logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.html)

</p>

---
## 주요 기능

- **자동 리스너 등록** — `craftBukkit`을 상속한 클래스를 자동으로 탐색합니다.
- **수동 등록 불필요** — `registerEvents()`를 직접 호출할 필요가 없습니다.
- **편리한 인스턴트** - '이제 메인 클래스에서 인스턴트를 지원을 할 필요가 없습니다.
- **플러그인 관리** — 명령어로 플러그인을 시작, 종료, 리로드할 수 있습니다.
- **간편한 적용** — 기존 Paper 플러그인에도 쉽게 적용할 수 있습니다.

## 사용 방법

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

public class JoinListener extends craftBukkit {

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
    }.runTaskLater(AutoListen.AutoIns(), 1L);
}

```

## 명령어

| 명령어 | 설명 |
|---|---|
| `/listen start <플러그인명>` | 지정한 플러그인 리스너 활성화 |
| `/listen stop <플러그인명>` | 지정한 플러그인 리스너 비활성화 |
| `/listen reload <플러그인명>` | 지정한 플러그인 비활성화 후 다시 활성화 |
| `/listen enable <플러그인명>` | 지정한 플러그인 활성화 |
| `/listen disable <플러그인명>` | 지정한 플러그인 비활성화 |

## 참고 사항

- 리스너 클래스는 반드시 `craftBukkit`을 상속해야 합니다.
- 추상 클래스는 자동 등록 대상에서 제외됩니다.
- 리스너 클래스에는 접근 가능한 매개변수 없는 생성자가 필요합니다.
- AutoListen은 다른 플러그인보다 먼저 로드되어야 합니다.
- 리로드는 Bukkit의 비활성화 및 활성화 방식이며 JAR 파일 자체를 다시 읽지는 않습니다.
- Kotlin문법은 자체적으로 지원하지는 않습니다, 직접 의존성을 추가해야합니다.

---

<div align="center">

### AutoListen

</div>

@Copyright by rkdgus127
