package dev.bauhd.teamchat.paper;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import io.github.miniplaceholders.api.MiniPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperTeamChat extends JavaPlugin implements TeamChatCommon<CommandSender> {

  private Configuration configuration;
  private boolean miniPlaceholders;
  private boolean placeholderApi;

  @Override
  public void onEnable() {
    final YamlConfiguration config = YamlConfiguration
        .loadConfiguration(Configuration.ensurePathIsValid(this.getDataFolder().toPath()).toFile());
    this.configuration = new Configuration(
        config.getString("prefix"),
        config.getString("permission"),
        config.getStringList("aliases"),
        config.getBoolean("announce-in-console"),
        config.getString("format"),
        config.getString("no-permission"),
        config.getString("usage")
    );

    this.getServer().getCommandMap().register("teamchat", new TeamChatCommand(this));

    final PluginManager pluginManager = this.getServer().getPluginManager();
    this.miniPlaceholders = pluginManager.isPluginEnabled("MiniPlaceholderAPI");
    this.placeholderApi = pluginManager.isPluginEnabled("PlaceholderAPI");
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }

  @Override
  public void addAdditionalResolver(
      CommandSender sender, Audience audience, TagResolver.Builder builder
  ) {
    if (this.miniPlaceholders) {
      builder.resolver(MiniPlaceholders.getAudiencePlaceholders(audience));
    }
    if (this.placeholderApi && sender instanceof Player player) {
      builder.resolver(TagResolver.resolver("papi", (argument, context) -> {
        final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player,
            '%' + argument.popOr("papi tag requires an argument").value() + '%');
        return Tag.selfClosingInserting(
            LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder));
      }));
    }
  }
}
