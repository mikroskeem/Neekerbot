/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot

import com.jtelegram.api.TelegramBotRegistry
import com.jtelegram.api.update.PollingUpdateProvider
import eu.mikroskeem.neekerbot.commands.AlwaysCommand
import eu.mikroskeem.neekerbot.commands.DadJokeCommand
import eu.mikroskeem.neekerbot.commands.DoRubyCommand
import eu.mikroskeem.neekerbot.commands.SummonCommand
import eu.mikroskeem.neekerbot.listeners.LoggingListener
import okhttp3.OkHttpClient
import java.net.URL
import javax.script.ScriptEngineManager

/**
 * @author Mark Vainomaa
 */
object NeekerBot {
    @JvmStatic
    fun main(vararg args: String) {
        registerBot()
    }
}

private val KEY = System.getenv("BOT_KEY") ?: throw RuntimeException("BOT_KEY env variable is missing!")

val registry = TelegramBotRegistry.builder()
        .updateProvider(PollingUpdateProvider())
        .build()

val jruby = ScriptEngineManager().getEngineByName("jruby")
val okhttp = OkHttpClient.Builder().build()

fun registerBot() {
    registry.registerBot(KEY) { bot, error ->
        if(error != null)
            throw error

        // Register commands here
        bot registerCommand AlwaysCommand::class
        bot registerCommand DadJokeCommand::class
        bot registerCommand DoRubyCommand::class
        bot registerCommand SummonCommand::class

        // Register listeners here
        bot registerListener LoggingListener::class

        bot registerListener TextContainsListener(listOf("neeger")) { _, chat, _, messageId, _ ->
            bot.sendText(chat, "iksdeeeeee", replyTo = messageId)
        }

        bot registerListener TextContainsListener(listOf("lel")) { _, chat, _, messageId, _ ->
            bot.sendDocument(chat, URL("https://i.imgur.com/fZVzuQh.gif"), replyTo = messageId)
        }

        bot registerListener TextRegexListener("(?i)r(e)\\1{3,}") { _, chat, _, messageId, _ ->
            bot.sendDocument(chat, URL("https://i.imgur.com/raxpPZa.gif"), replyTo = messageId)
        }

        bot registerListener TextEqualsListener("kp") { _, chat, _, messageId, _ ->
            bot.sendText(chat, "tra need 2 sõna kirjutatakse välja kuradi autist", replyTo = messageId)
        }
    }
}