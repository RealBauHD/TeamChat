package dev.bauhd.teamchat.bukkit;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
        config.getString("usage")
    );

    final PluginCommand command = this.getCommand("teamchat");
    assert command != null;
    command.setExecutor(this);
    command.setAliases(this.configuration.aliases());

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

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] arguments) {
    if (sender.hasPermission(this.configuration.permission())) {
      if (arguments.length == 0) {
        this.audiences.sender(sender).sendMessage(this.configuration.usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        final Component message = this.configuration.prefix().append(this
            .constructMessage(sender, this.audiences.sender(sender), sender.getName(),
                messageBuilder.toString()));
        for (final Player player : this.getServer().getOnlinePlayers()) {
          if (player.hasPermission(this.configuration.permission())) {
            this.audiences.player(player).sendMessage(message);
          }
        }
        if (this.configuration.announceInConsole()) {
          this.audiences.sender(sender).sendMessage(message);
        }
      }
    } else {
      this.audiences.sender(sender).sendMessage(this.configuration.noPermission());
    }
    return true;
  }
}
