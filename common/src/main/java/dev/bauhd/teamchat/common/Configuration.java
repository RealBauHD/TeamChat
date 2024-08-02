package dev.bauhd.teamchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Configuration {

  private final Component prefix;
  private final String permission;
  private final String format;
  private final Component noPermission;
  private final Component usage;

  public Configuration(
      final String prefix,
      final String permission,
      final String format,
      final String noPermission,
      final String usage
  ) {
    this.prefix = MiniMessage.miniMessage().deserialize(prefix);
    this.permission = permission;
    this.format = format;
    this.noPermission = this.prefix.append(MiniMessage.miniMessage().deserialize(noPermission));
    this.usage = this.prefix.append(MiniMessage.miniMessage().deserialize(usage));
  }

  public Component prefix() {
    return this.prefix;
  }

  public String permission() {
    return this.permission;
  }

  public String format() {
    return this.format;
  }

  public Component noPermission() {
    return this.noPermission;
  }

  public Component usage() {
    return this.usage;
  }
}
