/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.events.Event
import com.jtelegram.api.events.EventHandler
import com.jtelegram.api.events.message.MessageEvent
import com.jtelegram.api.events.message.TextMessageEvent
import com.jtelegram.api.user.User
import java.util.Locale
import java.util.regex.Pattern
import kotlin.reflect.KClass

/**
 * @author Mark Vainomaa
 */
interface Listener<T: Event>: EventHandler<T> {
    val eventType: KClass<T>
    val shouldHandle: (T) -> Boolean
    fun handle(event: T)

    override fun onEvent(event: T) {
        if(event is MessageEvent<*>) {
            if(currentUnixTime - event.message.date < 15)
                return
        }

        if(shouldHandle(event))
            handle(event)
    }
}

class TextEqualsListener(val word: String, val ignoreCase: Boolean = true,
                           private val handler: (bot: TelegramBot, chat: Chat, sender: User, messageId: Int, text: String) -> Unit
): Listener<TextMessageEvent> {
    override val eventType = TextMessageEvent::class
    override val shouldHandle: (TextMessageEvent) -> Boolean = { event ->
        event.message.content.run { if(ignoreCase) toLowerCase(Locale.ENGLISH) else this } == word
    }

    override fun handle(event: TextMessageEvent) = handler(event.bot, event.message.chat, event.message.sender, event.message.messageId, event.message.content)
}

class TextContainsListener(val words: List<String>, val ignoreCase: Boolean = true,
                           private val handler: (bot: TelegramBot, chat: Chat, sender: User, messageId: Int, text: String) -> Unit
): Listener<TextMessageEvent> {
    override val eventType = TextMessageEvent::class
    override val shouldHandle: (TextMessageEvent) -> Boolean = { event ->
        words.any { event.message.content.run { if(ignoreCase) toLowerCase(Locale.ENGLISH) else this }.contains(it) }
    }

    override fun handle(event: TextMessageEvent) = handler(event.bot, event.message.chat, event.message.sender, event.message.messageId, event.message.content)
}

class TextRegexListener(pattern: String, private val handler: (bot: TelegramBot, chat: Chat, sender: User, messageId: Int, text: String) -> Unit): Listener<TextMessageEvent> {
    private val regex = Regex(pattern)
    override val eventType = TextMessageEvent::class
    override val shouldHandle: (TextMessageEvent) -> Boolean = { it.message.content.matches(regex) }

    override fun handle(event: TextMessageEvent) = handler(event.bot, event.message.chat, event.message.sender, event.message.messageId, event.message.content)
}
