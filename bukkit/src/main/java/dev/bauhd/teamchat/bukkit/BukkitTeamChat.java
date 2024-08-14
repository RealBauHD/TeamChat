package dev.bauhd.teamchat.bukkit;

import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import java.util.Objects;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BukkitTeamChat extends JavaPlugin implements TeamChatCommon {

  private BukkitAudiences audiences;
  private Configuration configuration;

  @Override
  public void onEnable() {
    this.audiences = BukkitAudiences.create(this);

    final YamlConfiguration config = YamlConfiguration
        .loadConfiguration(Configuration.ensurePathIsValid(this.getDataFolder().toPath()).toFile());
    this.configuration = new Configuration(
        config.getString("prefix"),
        config.getString("permission"),
        config.getString("format"),
        config.getString("no-permission"),
        config.getString("usage")
    );

    Objects.requireNonNull(this.getCommand("teamchat")).setExecutor(this);
  }

  @Override
  public Configuration configuration() {
    return this.configuration;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String s, @NotNull String[] arguments) {
    if (sender.hasPermission(this.configuration().permission())) {
      if (arguments.length == 0) {
        this.audiences.sender(sender).sendMessage(this.configuration().usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        final Component message = this.configuration().prefix().append(this
            .constructMessage(this.audiences.sender(sender), sender.getName(),
                messageBuilder.toString()));
        for (final Player player : this.getServer().getOnlinePlayers()) {
          if (player.hasPermission(this.configuration().permission())) {
            this.audiences.player(player).sendMessage(message);
          }
        }
      }
    } else {
      this.audiences.sender(sender).sendMessage(this.configuration().noPermission());
    }
    return true;
  }
}
