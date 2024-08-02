package dev.bauhd.teamchat.common;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public interface TeamChatCommon {

  Configuration configuration();

  default Component constructMessage(final Audience audience, final String sender, final String message) {
    return MiniMessage.miniMessage().deserialize(this.configuration().format(),
        Placeholder.unparsed("sender", sender),
        Placeholder.unparsed("message", message));
  }
}
