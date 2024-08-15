package dev.bauhd.teamchat.bungee;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import java.io.IOException;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public final class BungeeTeamChat extends Plugin implements TeamChatCommon {

  private Configuration configuration;

  @Override
  public void onEnable() {
    try {
      final net.md_5.bungee.config.Configuration config = ConfigurationProvider
          .getProvider(YamlConfiguration.class).load(
          Configuration.ensurePathIsValid(this.getDataFolder().toPath()).toFile());
      this.configuration = new Configuration(
          config.getString("prefix"),
          config.getString("permission"),
          config.getBoolean("announce-in-console"),
          config.getString("format"),
          config.getString("no-permission"),
          config.getString("usage")
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.getProxy().getPluginManager()
        .registerCommand(this, new TeamChatCommand(this, BungeeAudiences.create(this)));
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }
}
