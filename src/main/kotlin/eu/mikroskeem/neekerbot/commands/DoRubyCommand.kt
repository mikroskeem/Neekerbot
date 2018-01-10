/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.commands

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.user.User
import eu.mikroskeem.neekerbot.AbstractCommand
import eu.mikroskeem.neekerbot.argsAsString
import eu.mikroskeem.neekerbot.jruby
import eu.mikroskeem.neekerbot.sendText
import java.util.concurrent.Executors

/**
 * @author Mark Vainomaa
 */
private val executorService = Executors.newCachedThreadPool()

class DoRubyCommand: AbstractCommand("doruby") {
    override fun execute(bot: TelegramBot, chat: Chat, sender: User, messageId: Int, commandName: String, args: List<String>) {
        executorService.submit {
            val res = jruby.eval(args.argsAsString).toString()
            bot.sendText(chat, res, messageId)
        }
    }
}