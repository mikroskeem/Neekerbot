/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.commands

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.user.User
import eu.mikroskeem.neekerbot.AbstractCommand
import eu.mikroskeem.neekerbot.sendPicture
import java.net.URL

/**
 * Sends picture of Always to chat
 *
 * @author Mark Vainomaa
 */
class AlwaysCommand: AbstractCommand("always") {
    private val alwaysPicture = URL("https://i.imgur.com/BoVfl7R.png")

    override fun execute(bot: TelegramBot, chat: Chat, sender: User, messageId: Int, commandName: String, args: List<String>) {
        bot.sendPicture(chat, alwaysPicture)
    }
}