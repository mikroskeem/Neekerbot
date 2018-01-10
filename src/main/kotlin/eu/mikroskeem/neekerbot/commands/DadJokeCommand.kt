/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package eu.mikroskeem.neekerbot.commands

import com.jtelegram.api.TelegramBot
import com.jtelegram.api.chat.Chat
import com.jtelegram.api.user.User
import eu.mikroskeem.neekerbot.AbstractCommand
import eu.mikroskeem.neekerbot.okhttp
import eu.mikroskeem.neekerbot.sendText
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author Mark Vainomaa
 */
class DadJokeCommand: AbstractCommand("dad") {
    private val request = Request.Builder()
            .get()
            .addHeader("Accept", "text/plain")
            .addHeader("User-Agent", "NeekerBot")
            .url(HttpUrl.parse("https://icanhazdadjoke.com/")!!)
            .build()

    override fun execute(bot: TelegramBot, chat: Chat, sender: User, messageId: Int, commandName: String, args: List<String>) {
        okhttp.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, exception: IOException) {
                bot.sendText(chat, "Failed to get dad joke :( $exception", messageId)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    bot.sendText(chat, response.body()!!.string())
                } else {
                    bot.sendText(chat, "Failed to get dad joke :( HTTP code: ${response.code()}", messageId)
                }
            }
        })
    }
}