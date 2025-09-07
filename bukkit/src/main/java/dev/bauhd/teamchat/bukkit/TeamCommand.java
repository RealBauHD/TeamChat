package dev.bauhd.teamchat.bukkit;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class TeamCommand implements CommandExecutor {

  private final BukkitTeamChat teamChat;

  TeamCommand(final BukkitTeamChat teamChat) {
    this.teamChat = teamChat;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] arguments
  ) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      final Audience audience = this.teamChat.audiences().sender(sender);
      for (final Player player : this.teamChat.getServer().getOnlinePlayers()) {
        if (player.hasPermission(this.teamChat.configuration().permission())) {
          final TagResolver.Builder tagResolverBuilder = TagResolver.builder()
              .resolver(Placeholder.unparsed("player", player.getName()))
              .resolver(Placeholder.unparsed("location", player.getWorld().getName()));
          this.teamChat.addAdditionalResolver(player, tagResolverBuilder);
          audience.sendMessage(this.teamChat.configuration().prefix()
              .append(MiniMessage.miniMessage().deserialize(
                  this.teamChat.configuration().teamMessage(),
                  this.teamChat.audiences().player(player), tagResolverBuilder.build())));
        }
      }
    }
    return true;
  }
}
