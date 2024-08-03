package dev.bauhd.teamchat.paper;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTeamChat extends JavaPlugin implements TeamChatCommon {

  private boolean miniPlaceholders;

  @Override
  public void onEnable() {
    this.getServer().getCommandMap().register("teamchat", new TeamChatCommand(this));

    this.miniPlaceholders = this.getServer().getPluginManager().getPlugin("MiniPlaceholderAPI") != null;
  }

  @Override
  public Configuration configuration() {
    return new Configuration("<dark_gray>[<blue>TeamChat</blue>] ", "teamchat.use",
        "<yellow><sender></yellow> <dark_gray>»</dark_gray> <gray><message></gray>", "<red>No permission!", "<red>Usage: /teamchat <Message>");
  }

  @Override
  public void addAdditionalResolver(Audience audience, TagResolver.Builder builder) {
    if (this.miniPlaceholders) {
      builder.resolver(MiniPlaceholders.getAudiencePlaceholders(audience));
    }
  }
}
