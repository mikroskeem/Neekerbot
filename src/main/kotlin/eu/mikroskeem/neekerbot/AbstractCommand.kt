/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.commands.Command
import com.jtelegram.api.commands.filters.CommandFilter
import com.jtelegram.api.events.message.TextMessageEvent
import com.jtelegram.api.user.User

/**
 * A command class
 *
 * @author Mark Vainomaa
 */
interface Command: CommandFilter {
    val commandName: String
    val aliases: Array<out String>

    /**
     * Command handler method
     */
    fun execute(bot: TelegramBot, chat: Chat, sender: User, messageId: Int, commandName: String, args: List<String>)

    override fun test(event: TextMessageEvent, command: Command): Boolean {
        if(command.baseCommand == commandName || aliases.contains(command.baseCommand)) {
            // Don't try to process message which was sent more than 15 seconds ago
            if(currentUnixTime - event.message.date > 15)
                return false

            try {
                // Try to execute command
                execute(event.bot, event.message.chat, event.message.sender, event.message.messageId, commandName, command.args)
                return true
            } catch (e: Exception) {
                // TODO: log
            }
        }
        return false
    }
}

abstract class AbstractCommand(override val commandName: String, override vararg val aliases: String): eu.mikroskeem.neekerbot.Command {

}