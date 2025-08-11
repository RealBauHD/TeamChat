package dev.bauhd.teamchat.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

final class TeamChatCommand extends Command {

  private final BungeeTeamChat teamChat;
  private final BungeeAudiences audiences;

  TeamChatCommand(final BungeeTeamChat teamChat, final BungeeAudiences audiences) {
    super("teamchat", teamChat.configuration().permission(),
        teamChat.configuration().aliases().toArray(new String[0]));
    this.teamChat = teamChat;
    this.audiences = audiences;
  }

  @Override
  public void execute(CommandSender sender, String[] arguments) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      if (arguments.length == 0) {
        this.audiences.sender(sender).sendMessage(this.teamChat.configuration().usage());
      } else {
        final StringBuilder messageBuilder = new StringBuilder();
        for (final String argument : arguments) {
          messageBuilder.append(argument).append(" ");
        }

        final Component message = this.teamChat.configuration().prefix().append(this.teamChat
            .constructMessage(sender, this.audiences.sender(sender), sender.getName(),
                messageBuilder.toString()));
        for (final ProxiedPlayer player : this.teamChat.getProxy().getPlayers()) {
          if (player.hasPermission(this.teamChat.configuration().permission())) {
            this.audiences.player(player).sendMessage(message);
          }
        }
        if (this.teamChat.configuration().announceInConsole()) {
          this.audiences.sender(this.teamChat.getProxy().getConsole()).sendMessage(message);
        }
      }
    } else {
      this.audiences.sender(sender).sendMessage(this.teamChat.configuration().noPermission());
    }
  }
}
