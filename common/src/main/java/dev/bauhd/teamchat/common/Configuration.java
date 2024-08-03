package dev.bauhd.teamchat.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Configuration {

  private static final String FILE_NAME = "config.yml";

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

  public static Path ensurePathIsValid(final Path dataDirectory) {
    try {
      if (Files.notExists(dataDirectory)) {
        Files.createDirectory(dataDirectory);
      }
      final Path configPath = dataDirectory.resolve(FILE_NAME);
      if (Files.notExists(configPath)) {
        final InputStream inputStream = Configuration.class.getClassLoader()
            .getResourceAsStream(FILE_NAME);
        assert inputStream != null;

        Files.copy(inputStream, configPath);
      }
      return configPath;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
