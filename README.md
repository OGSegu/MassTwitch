# [Twitch](https://twitch.tv) Follow Bot

This tool allows you to manipulate with follows in accounts using tokens.

# Requirements

- Java (JDK) 8 or later

# Download

You can download prebuilt binaries from [GitHub releases](https://github.com/OGSegu/TwitchTool/releases).

# Usage

Checker:

- Check accounts from default .txt file ("tokens.txt")
```bash
java -jar TwitchFollowBot.jar check
```

- Check accounts from custom .txt file
```bash
java -jar TwitchFollowBot.jar check filename.txt
```

Follower:

- Follow to channel from default .txt file ("valid.txt")
```bash
java -jar TwitchFollowBot.jar follow channelname amount
```

- Follow to channel from custom .txt file
```bash
java -jar TwitchFollowBot.jar follow filename.txt channelname amount
```
