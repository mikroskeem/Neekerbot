/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.listeners

import com.jtelegram.api.events.message.TextMessageEvent
import eu.mikroskeem.neekerbot.TextEqualsListener
import eu.mikroskeem.neekerbot.getRandomElement
import eu.mikroskeem.neekerbot.sendText

/**
 * @author Mark Vainomaa
 */
private val triggers = listOf("neeger", "neeker", "nignog")
private val responses = listOf(
        "no u",
        "iksdeee",
        "neekeri bljät",
        "mis tahad türa"
)

class NeegerTextListener: TextEqualsListener(triggers) {
    override fun handle(event: TextMessageEvent) {
        event.bot.sendText(event.message.chat, responses.getRandomElement(), replyTo = event.message.messageId)
    }
}