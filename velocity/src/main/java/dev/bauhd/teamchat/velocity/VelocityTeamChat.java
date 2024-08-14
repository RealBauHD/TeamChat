package dev.bauhd.teamchat.velocity;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.bauhd.teamchat.common.Configuration;
import dev.bauhd.teamchat.common.TeamChatCommon;
import io.github.miniplaceholders.api.MiniPlaceholders;
import java.io.IOException;
import java.nio.file.Path;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@Plugin(
    id = "teamchat",
    name = "TeamChat",
    authors = "BauHD",
    version = "1.0",
    url = "https://modrinth.com/project/teamchat",
    dependencies = {
        @Dependency(id = "miniplaceholders", optional = true)
    }
)
public final class VelocityTeamChat implements TeamChatCommon {

  private final ProxyServer proxyServer;
  private final Path dataDirectory;
  private Configuration configuration;
  private boolean miniPlaceholders;

  @Inject
  public VelocityTeamChat(final ProxyServer proxyServer, final @DataDirectory Path dataDirectory) {
    this.proxyServer = proxyServer;
    this.dataDirectory = dataDirectory;
  }

  @Subscribe
  public void handleInit(final ProxyInitializeEvent event) {
    this.miniPlaceholders = this.proxyServer.getPluginManager().isLoaded("miniplaceholders");

    try {
      final CommentedConfigurationNode node = YamlConfigurationLoader.builder()
          .path(Configuration.ensurePathIsValid(this.dataDirectory)).build().load();
      this.configuration = new Configuration(
          node.node("prefix").getString(),
          node.node("permission").getString(),
          node.node("format").getString(),
          node.node("no-permission").getString(),
          node.node("usage").getString()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.proxyServer.getCommandManager()
        .register("tc", new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("teamchat")
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
    return this.configuration;
  }

  @Override
  public void addAdditionalResolver(Audience audience, TagResolver.Builder builder) {
    if (this.miniPlaceholders) {
      builder.resolver(MiniPlaceholders.getAudiencePlaceholders(audience));
    }
  }
}
