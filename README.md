<div align="center">

# AutoListen

### Automatic Listener Registration for Paper Plugins

**Stop registering listeners manually. Let AutoListen handle it.**

**리스너를 일일이 등록하지 마세요. AutoListen이 대신 처리합니다.**

<br>

![Minecraft](https://img.shields.io/badge/Minecraft-26.3-brightgreen?style=flat-square)
![Platform](https://img.shields.io/badge/Platform-Paper-blue?style=flat-square)
![Java](https://img.shields.io/badge/Java-27-orange?style=flat-square)

</div>

---

# 🇰🇷 한국어

## 소개

AutoListen은 Paper 서버에서 리스너 등록을 자동화하기 위해 제작된 플러그인입니다.

기존에는 리스너를 만들 때마다 `registerEvents()`를 직접 호출해야 했지만, AutoListen을 사용하면 이러한 작업이 필요하지 않습니다.

단순히 `craftBukkit` 클래스를 상속하는 것만으로 리스너가 자동으로 탐색되고 등록됩니다.

또한 명령어를 통해 다른 플러그인을 시작, 종료, 리로드할 수 있습니다.

## 주요 기능

- **자동 리스너 등록** — `craftBukkit`을 상속한 클래스를 자동으로 탐색합니다.
- **수동 등록 불필요** — `registerEvents()`를 직접 호출할 필요가 없습니다.
- **플러그인 관리** — 명령어로 플러그인을 시작, 종료, 리로드할 수 있습니다.
- **간편한 적용** — 기존 Paper 플러그인에도 쉽게 적용할 수 있습니다.

## 설치 방법

1. `AutoListen.jar` 파일을 다운로드합니다.
2. 마인크래프트 서버의 `plugins` 폴더에 넣습니다.
3. 서버를 재시작합니다.

## 사용 방법

### 1. 프로젝트에 AutoListen 추가

프로젝트 내부에 `libs` 폴더를 만들고 `AutoListen.jar`를 넣습니다.

`build.gradle.kts`에 다음 내용을 추가합니다.

```kotlin
dependencies {
    compileOnly(files("libs/AutoListen.jar"))
}
```

`plugin.yml`에도 의존성을 추가합니다.

```yaml
depend: [AutoListen]
```

### 2. 리스너 작성

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

### 3. 끝!

이제 별도의 리스너 등록 코드를 작성할 필요가 없습니다.

AutoListen이 플러그인 내부의 클래스를 탐색하고 자동으로 등록합니다.

## 명령어

| 명령어 | 설명 |
|---|---|
| `/listen start <플러그인명>` | 플러그인 활성화 |
| `/listen stop <플러그인명>` | 플러그인 비활성화 |
| `/listen reload <플러그인명>` | 플러그인 비활성화 후 다시 활성화 |

**권한:** `autolisten.admin` (기본값: OP)

## 요구 사항

- Paper 서버
- Java 27
- 서버에 AutoListen 설치

## 참고 사항

- 리스너 클래스는 반드시 `craftBukkit`을 상속해야 합니다.
- 추상 클래스는 자동 등록 대상에서 제외됩니다.
- 리스너 클래스에는 접근 가능한 매개변수 없는 생성자가 필요합니다.
- AutoListen은 다른 플러그인보다 먼저 로드되어야 합니다.
- 리로드는 Bukkit의 비활성화 및 활성화 방식이며 JAR 파일 자체를 다시 읽지는 않습니다.

---
# 🇺🇸 English

## Overview

AutoListen is a lightweight Paper plugin designed to automate listener registration.

Simply extend `craftBukkit` in your listener classes. AutoListen will automatically discover and register them without requiring manual registration.

It also provides commands to start, stop, and reload other installed plugins.

## Features

- **Automatic Listener Registration** — Automatically discovers classes extending `craftBukkit`.
- **No Manual Registration** — No need to call `registerEvents()` manually.
- **Plugin Management** — Start, stop, and reload plugins through commands.
- **Easy Integration** — Simple to integrate into existing Paper plugins.

## Installation

1. Download `AutoListen.jar`.
2. Place it inside your server's `plugins` folder.
3. Restart your server.

## Getting Started

### 1. Add AutoListen to Your Project

Place `AutoListen.jar` inside your project's `libs` folder.

Add the following to `build.gradle.kts`:

```kotlin
dependencies {
    compileOnly(files("libs/AutoListen.jar"))
}
```

Add AutoListen as a dependency in `plugin.yml`:

```yaml
depend: [AutoListen]
```

### 2. Create a Listener

Extend `craftBukkit` instead of implementing Bukkit's `Listener` directly.

```java
package example.Event;

import rkdgus.Listener.craftBukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends craftBukkit {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Welcome to the server!");
    }
}
```

### 3. Done!

That's all. AutoListen automatically discovers and registers your listener.

No manual registration is required.

## Commands

| Command | Description |
|---|---|
| `/listen start <plugin>` | Enables a plugin |
| `/listen stop <plugin>` | Disables a plugin |
| `/listen reload <plugin>` | Disables and enables a plugin |

**Permission:** `autolisten.admin` (Default: OP)

## Requirements

- Paper
- Java 27
- AutoListen installed on the server

## Notes

- Listener classes must extend `craftBukkit`.
- Abstract classes are ignored.
- Listener classes require an accessible no-argument constructor.
- AutoListen must be loaded before dependent plugins.
- Reload uses Bukkit's enable/disable mechanism and does not reload the JAR from disk.
---

<div align="center">

### AutoListen

**Less boilerplate. More development.**

**불필요한 코드는 줄이고, 개발에 집중하세요.**

</div>

@Copyright by rkdgus127