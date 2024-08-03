package dev.bauhd.teamchat.common;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface TeamChatCommon {

  Configuration configuration();

  default Component constructMessage(
      final Audience audience, final String sender, final String message
  ) {
    final TagResolver.Builder tagResolverBuilder = TagResolver.builder()
        .resolver(Placeholder.unparsed("sender", sender))
        .resolvers(Placeholder.unparsed("message", message));
    this.addAdditionalResolver(audience, tagResolverBuilder);
    return MiniMessage.miniMessage().deserialize(
        this.configuration().format(), tagResolverBuilder.build());
  }

  default void addAdditionalResolver(final Audience audience, final TagResolver.Builder builder) {
  }
}
