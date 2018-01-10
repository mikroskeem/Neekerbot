/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.events.Event
import com.jtelegram.api.message.input.file.ExternalInputFile
import com.jtelegram.api.requests.message.send.SendDocument
import com.jtelegram.api.requests.message.send.SendPhoto
import com.jtelegram.api.requests.message.send.SendText
import java.net.URL
import java.time.Instant
import kotlin.reflect.KClass

/**
 * Shorthand utilities
 *
 * @author Mark Vainomaa
 */

val currentUnixTime: Long get() = Instant.now().epochSecond
val List<String>.argsAsString get() = this.joinToString(separator = " ")

infix fun TelegramBot.registerCommand(command: Command) = this.commandRegistry.registerCommand(command.commandName, command)
infix fun <T: Command> TelegramBot.registerCommand(command: KClass<T>) = registerCommand(command.java.getConstructor().newInstance())
infix fun <E: Event, T: Listener<E>> TelegramBot.registerListener(listener: T) = eventRegistry.registerEvent(listener.eventType.java, listener)
infix fun <E: Event, T: Listener<E>> TelegramBot.registerListener(listener: KClass<T>) = registerListener(listener.java.getConstructor().newInstance())

/**
 * Sends text
 */
fun TelegramBot.sendText(chat: Chat, text: String, replyTo: Int? = null) {
    perform(SendText.builder()
            .chatId(chat.chatId)
            .run { if(replyTo != null) replyToMessageID(replyTo) else this }
            .text(text)
            .build()
    )
}

/**
 * Sends picture
 *
 * Use [sendDocument] for GIFs
 */
fun TelegramBot.sendPicture(chat: Chat, url: URL, replyTo: Int? = null, caption: String? = null) {
    perform(SendPhoto.builder()
            .chatId(chat.chatId)
            .run { if(replyTo != null) replyToMessageId(replyTo) else this }
            .run { if(caption != null) caption(caption) else this }
            .photo(ExternalInputFile(url))
            .build()
    )
}

/**
 * Sends document
 */
fun TelegramBot.sendDocument(chat: Chat, url: URL, replyTo: Int? = null, caption: String? = null) {
    perform(SendDocument.builder()
            .chatId(chat.chatId)
            .run { if(replyTo != null) replyToMessageId(replyTo) else this }
            .run { if(caption != null) caption(caption) else this }
            .document(ExternalInputFile(url))
            .build()
    )
}