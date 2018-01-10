/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.commands

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.user.User
import eu.mikroskeem.neekerbot.AbstractCommand
import eu.mikroskeem.neekerbot.sendText

/**
 * @author Mark Vainomaa
 */
class SummonCommand: AbstractCommand("summon") {
    override fun execute(bot: TelegramBot, chat: Chat, sender: User, messageId: Int, commandName: String, args: List<String>) {
        bot.sendText(chat, "@Ciukoer")
    }
}