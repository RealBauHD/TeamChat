package dev.bauhd.teamchat.paper;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TeamChatCommand extends Command {

  private final PaperTeamChat teamChat;

  TeamChatCommand(final PaperTeamChat teamChat) {
    super("teamchat", "The TeamChat command.", "/teamchat <Message>", List.of("tc"));
    this.teamChat = teamChat;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender sender, @NotNull String s, @NotNull String[] arguments
  ) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      if (arguments.length == 0) {
        sender.sendMessage(this.teamChat.configuration().usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        for (final Player player : this.teamChat.getServer().getOnlinePlayers()) {
          if (player.hasPermission(this.teamChat.configuration().permission())) {
            player.sendMessage(this.teamChat.configuration().prefix().append(this.teamChat
                .constructMessage(player, sender.getName(), messageBuilder.toString())));
          }
        }
      }
    } else {
      sender.sendMessage(this.teamChat.configuration().noPermission());
    }
    return true;
  }
}
