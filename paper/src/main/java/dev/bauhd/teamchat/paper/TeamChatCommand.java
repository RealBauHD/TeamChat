package dev.bauhd.teamchat.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class TeamChatCommand extends Command {

  private final PaperTeamChat teamChat;

  TeamChatCommand(final PaperTeamChat teamChat) {
    super("teamchat", "The TeamChat command.", "/teamchat <Message>",
        teamChat.configuration().aliases());
    this.teamChat = teamChat;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender sender, @NotNull String label, @NotNull String[] arguments
  ) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      if (arguments.length == 0) {
        sender.sendMessage(this.teamChat.configuration().usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        final Component message = this.teamChat.configuration().prefix().append(this.teamChat
            .constructMessage(sender, sender, sender.getName(), messageBuilder.toString()));
        for (final Player player : this.teamChat.getServer().getOnlinePlayers()) {
          if (player.hasPermission(this.teamChat.configuration().permission())) {
            player.sendMessage(message);
          }
        }
        if (this.teamChat.configuration().announceInConsole()) {
          this.teamChat.getServer().getConsoleSender().sendMessage(message);
        }
      }
    } else {
      sender.sendMessage(this.teamChat.configuration().noPermission());
    }
    return true;
  }
}
