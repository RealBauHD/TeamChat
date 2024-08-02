package dev.bauhd.teamchat.velocity;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import java.nio.file.Path;

@Plugin(
    id = "teamchat",
    name = "TeamChat",
    authors = "BauHD",
    version = "1.0",
    url = "https://hangar.papermc.io/BauHD/TeamChat")
public final class VelocityTeamChat implements TeamChatCommon {

  private final ProxyServer proxyServer;
  private final Path dataDirectory;

  @Inject
  public VelocityTeamChat(final ProxyServer proxyServer, final @DataDirectory Path dataDirectory) {
    this.proxyServer = proxyServer;
    this.dataDirectory = dataDirectory;
  }

  @Subscribe
  public void handleInit(final ProxyInitializeEvent event) {
    this.proxyServer.getCommandManager()
        .register(new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("teamchat")
            .requires(source -> source.hasPermission(this.configuration().permission()))
            .executes(context -> {
              context.getSource().sendMessage(this.configuration().usage());
              return Command.SINGLE_SUCCESS;
            })
            .then(BrigadierCommand.requiredArgumentBuilder("message",
                    StringArgumentType.greedyString())
                .executes(context -> {
                  final String name = (context.getSource() instanceof Player player
                      ? player.getUsername() : "CONSOLE");
                  final String message = context.getArgument("message", String.class);
                  for (final Player player : this.proxyServer.getAllPlayers()) {
                    if (player.hasPermission(this.configuration().permission())) {
                      player.sendMessage(this.configuration().prefix().append(this
                          .constructMessage(player, name, message)));
                    }
                  }
                  return Command.SINGLE_SUCCESS;
                }))
        ));
  }

  @Override
  public Configuration configuration() {
    return new Configuration("<dark_gray>[<blue>TeamChat</blue>] ", "teamchat.use",
        "<yellow><sender></yellow> <dark_gray>»</dark_gray> <gray><message></gray>",
        "<red>No permission!", "<red>Usage: /teamchat <Message>");
  }
}
