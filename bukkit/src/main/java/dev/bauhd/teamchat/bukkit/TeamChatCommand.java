package dev.bauhd.teamchat.bukkit;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class TeamChatCommand implements CommandExecutor {

  private final BukkitTeamChat teamChat;

  TeamChatCommand(final BukkitTeamChat teamChat) {
    this.teamChat = teamChat;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] arguments
  ) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      if (arguments.length == 0) {
        this.teamChat.audiences().sender(sender).sendMessage(this.teamChat.configuration().usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        final Component message = this.teamChat.configuration().prefix().append(
            this.teamChat.constructMessage(sender, this.teamChat.audiences().sender(sender),
                sender.getName(), messageBuilder.toString()));
        for (final Player player : this.teamChat.getServer().getOnlinePlayers()) {
          if (player.hasPermission(this.teamChat.configuration().permission())) {
            this.teamChat.audiences().player(player).sendMessage(message);
          }
        }
        if (this.teamChat.configuration().announceInConsole()) {
          this.teamChat.audiences().sender(sender).sendMessage(message);
        }
      }
    } else {
      this.teamChat.audiences().sender(sender)
          .sendMessage(this.teamChat.configuration().noPermission());
    }
    return true;
  }
}
