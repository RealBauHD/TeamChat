package dev.bauhd.teamchat.paper;

import java.util.List;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class TeamCommand extends Command {

  private final PaperTeamChat teamChat;

  TeamCommand(final PaperTeamChat teamChat) {
    super("team", "A command to display your team.", "/team", List.of());
    this.teamChat = teamChat;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender sender, @NotNull String label, @NotNull String[] arguments
  ) {
    if (sender.hasPermission(this.teamChat.configuration().permission())) {
      for (final Player player : this.teamChat.getServer().getOnlinePlayers()) {
        if (player.hasPermission(this.teamChat.configuration().permission())) {
          final TagResolver.Builder tagResolverBuilder = TagResolver.builder()
              .resolver(Placeholder.unparsed("player", player.getName()))
              .resolver(Placeholder.unparsed("location", player.getWorld().getName()));
          this.teamChat.addAdditionalResolver(player, player, tagResolverBuilder);
          sender.sendMessage(this.teamChat.configuration().prefix()
              .append(MiniMessage.miniMessage().deserialize(
                  this.teamChat.configuration().teamMessage(), tagResolverBuilder.build())));
        }
      }
    }
    return true;
  }
}
