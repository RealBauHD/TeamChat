# TeamChat
A plugin that provides a separate chat for the server team.

## Features
- /teamchat command
- [MiniMessage](https://docs.advntr.dev/minimessage/format.html)
- [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders) support for Paper and Velocity

## Requirements
- Java 17+

## Default configuration

```
# TeamChat plugin v2.2 by BauHD
# Modrinth: https://modrinth.com/project/teamchat
# Discord: https://discord.gg/Gmxwzz2rA9
prefix: "<dark_gray>[<gradient:blue:aqua>TeamChat</gradient>]</dark_gray> "
permission: "teamchat.use"
aliases:
  - "tc"
announce-in-console: false
format: "<yellow><sender></yellow> <dark_gray>»</dark_gray> <gray><message></gray>"
no-permission: "<red>You do not have the permission to use this command!</red>"
usage: "<red>Usage: /teamchat <Message></red>"
team-message: "<yellow><player></yellow> <dark_gray>-</dark_gray> <gray><location></gray>"
```
