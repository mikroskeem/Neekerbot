/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot

import ch.jamiete.mcping.MinecraftPing
import ch.jamiete.mcping.MinecraftPingOptions
import com.jtelegram.api.TelegramBotRegistry
import com.jtelegram.api.commands.filters.CommandFilter
import com.jtelegram.api.events.message.TextMessageEvent
import com.jtelegram.api.message.input.file.ExternalInputFile
import com.jtelegram.api.requests.message.send.SendPhoto
import com.jtelegram.api.requests.message.send.SendText
import com.jtelegram.api.update.PollingUpdateProvider
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

val sm = ScriptEngineManager()
val jruby = sm.getEngineByName("jruby")

fun registerBot() {
    registry.registerBot(KEY) { bot, error ->
        if(error != null)
            throw error


        bot.eventRegistry.registerEvent(TextMessageEvent::class.java) {
            println(it.message.content)
            if(it.message.content.contains("neeger", ignoreCase = true)) {
                bot.perform(SendText.builder()
                        .chatId(it.message.chat.chatId)
                        .replyToMessageID(it.message.messageId)
                        .text("iksdeeeeee")
                        .build()
                )
            }
        }

        bot.commandRegistry.registerCommand(CommandFilter { event, command ->
            when(command.baseCommand) {
                "kys" -> {
                    bot.perform(SendText.builder()
                            .chatId(event.message.chat.chatId)
                            .replyToMessageID(event.message.messageId)
                            .text("no u")
                            .build()
                    )
                }
                "always" -> {
                    bot.perform(SendPhoto.builder()
                            .chatId(event.message.chat.chatId)
                            .photo(ExternalInputFile(URL("https://i.imgur.com/BoVfl7R.png")))
                            .build()
                    )
                }
                "summon" -> {
                    bot.perform(SendText.builder()
                            .chatId(event.message.chat.chatId)
                            .text("@Ciukoer")
                            .build()
                    )
                }
                "doruby" -> {
                    val result = jruby.eval(command.argsAsText).toString()
                    bot.perform(SendText.builder()
                            .chatId(event.message.chat.chatId)
                            .replyToMessageID(event.message.messageId)
                            .text(result)
                            .build()
                    )
                }
                "ping" -> {
                    if(command.args.size < 1) {
                        bot.perform(SendText.builder()
                                .chatId(event.message.chat.chatId)
                                .replyToMessageID(event.message.messageId)
                                .text("Usage: <ip> [port]")
                                .build()
                        )
                    } else {
                        val ip = command.args[0]!!
                        val port: Int = if(command.args.size > 1) command.args[1]?.toIntOrNull() ?: 25565 else 25565

                        val result = MinecraftPing().getPing(MinecraftPingOptions()
                                .setHostname(ip)
                                .setPort(port)
                        )

                        val text = """
                        ${result.description?.text} - ${result.players?.run { "$online/$max" }}
                        """

                        bot.perform(SendText.builder()
                                .chatId(event.message.chat.chatId)
                                .replyToMessageID(event.message.messageId)
                                .text(text)
                                .build()
                        )
                    }
                }
            }

            return@CommandFilter true
        })
    }
}