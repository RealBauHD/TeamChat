package dev.bauhd.teamchat.bungee;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeTeamChat extends Plugin implements TeamChatCommon {

  @Override
  public void onEnable() {
    this.getProxy().getPluginManager()
        .registerCommand(this, new TeamChatCommand(this, BungeeAudiences.create(this)));
  }

  @Override
  public Configuration configuration() {
    return new Configuration("<dark_gray>[<blue>TeamChat</blue>] ", "teamchat.use",
        "<yellow><sender></yellow> <dark_gray>»</dark_gray> <gray><message></gray>",
        "<red>No permission!", "<red>Usage: /teamchat <Message>");
  }
}
