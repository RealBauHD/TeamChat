package dev.bauhd.teamchat.bukkit;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitTeamChat extends JavaPlugin implements TeamChatCommon<CommandSender> {

  private BukkitAudiences audiences;
  private Configuration configuration;
  private boolean placeholderApi;

  @Override
  public void onEnable() {
    this.audiences = BukkitAudiences.create(this);

    final YamlConfiguration config = YamlConfiguration
        .loadConfiguration(Configuration.ensurePathIsValid(this.getDataFolder().toPath()).toFile());
    this.configuration = new Configuration(
        config.getString("prefix"),
        config.getString("permission"),
        config.getStringList("aliases"),
        config.getBoolean("announce-in-console"),
        config.getString("format"),
        config.getString("no-permission"),
        config.getString("usage"),
        config.getString("team-message")
    );

    final PluginCommand command = this.getCommand("teamchat");
    assert command != null;
    command.setExecutor(new TeamChatCommand(this));
    command.setAliases(this.configuration.aliases());
    this.getCommand("team").setExecutor(new TeamCommand(this));

    this.placeholderApi = this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }

  @Override
  public void addAdditionalResolver(
      CommandSender sender, Audience audience, TagResolver.Builder builder
  ) {
    if (this.placeholderApi && sender instanceof Player player) {
      builder.resolver(TagResolver.resolver("papi", (argument, context) -> {
        final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player,
            '%' + argument.popOr("papi tag requires an argument").value() + '%');
        return Tag.selfClosingInserting(
            LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder));
      }));
    }
  }

  public BukkitAudiences audiences() {
    return this.audiences;
  }
}
