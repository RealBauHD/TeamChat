package dev.bauhd.teamchat.paper;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTeamChat extends JavaPlugin implements TeamChatCommon {

  @Override
  public void onEnable() {
    this.getServer().getCommandMap().register("teamchat", new TeamChatCommand(this));
  }

  @Override
  public Configuration configuration() {
    return new Configuration("<dark_gray>[<blue>TeamChat</blue>] ", "teamchat.use",
        "<yellow><sender></yellow> <dark_gray>»</dark_gray> <gray><message></gray>", "<red>No permission!", "<red>Usage: /teamchat <Message>");
  }
}
