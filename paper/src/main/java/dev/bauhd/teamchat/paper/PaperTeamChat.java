package dev.bauhd.teamchat.paper;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTeamChat extends JavaPlugin implements TeamChatCommon {

  private Configuration configuration;
  private boolean miniPlaceholders;

  @Override
  public void onEnable() {
    final YamlConfiguration config = YamlConfiguration
        .loadConfiguration(Configuration.ensurePathIsValid(this.getDataFolder().toPath()).toFile());
    this.configuration = new Configuration(
        config.getString("prefix"),
        config.getString("permission"),
        config.getString("format"),
        config.getString("no-permission"),
        config.getString("usage")
    );

    this.getServer().getCommandMap().register("teamchat", new TeamChatCommand(this));

    this.miniPlaceholders = this.getServer().getPluginManager()
        .getPlugin("MiniPlaceholderAPI") != null;
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }

  @Override
  public void addAdditionalResolver(Audience audience, TagResolver.Builder builder) {
    if (this.miniPlaceholders) {
      builder.resolver(MiniPlaceholders.getAudiencePlaceholders(audience));
    }
  }
}
