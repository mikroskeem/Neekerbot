/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.listeners

import com.jtelegram.api.events.message.TextMessageEvent
import eu.mikroskeem.neekerbot.Listener

/**
 * @author Mark Vainomaa
 */
class LoggingListener: Listener<TextMessageEvent> {
    override val eventType = TextMessageEvent::class
    override val shouldHandle: (TextMessageEvent) -> Boolean = { true }

    override fun handle(event: TextMessageEvent) {
        println("[${event.message.chat.username}] ${event.message.sender.fullname} (${event.message.sender.username.takeUnless { it.isEmpty() } ?: "[no username]"}) -> ${event.message.content}")
    }
}