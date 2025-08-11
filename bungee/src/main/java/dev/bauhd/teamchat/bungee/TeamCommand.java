package dev.bauhd.teamchat.bungee;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

final class TeamCommand extends Command {

  private final BungeeTeamChat teamChat;
  private final BungeeAudiences audiences;

  TeamCommand(final BungeeTeamChat teamChat, final BungeeAudiences audiences) {
    super("team");
    this.teamChat = teamChat;
    this.audiences = audiences;
  }

  @Override
  public void execute(CommandSender sender, String[] arguments) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      final Audience audience = this.audiences.sender(sender);
      for (final ProxiedPlayer player : this.teamChat.getProxy().getPlayers()) {
        if (player.hasPermission(this.teamChat.configuration().permission())) {
          final TagResolver.Builder tagResolverBuilder = TagResolver.builder()
              .resolver(Placeholder.unparsed("player", player.getName()))
              .resolver(Placeholder.unparsed("location", player.getServer().getInfo().getName()));
          this.teamChat.addAdditionalResolver(player, this.audiences.player(player),
              tagResolverBuilder);
          audience.sendMessage(this.teamChat.configuration().prefix()
              .append(MiniMessage.miniMessage().deserialize(
                  this.teamChat.configuration().teamMessage(), tagResolverBuilder.build())));
        }
      }
    }
  }
}
