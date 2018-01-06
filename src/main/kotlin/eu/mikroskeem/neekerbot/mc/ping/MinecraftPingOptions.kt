/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package ch.jamiete.mcping

/**
 * Storage class for [MinecraftPing] options.
 */
class MinecraftPingOptions {

    private var hostname: String? = null
    private var port = 25565
    private var timeout = 2000
    private var charset = "UTF-8"

    fun setHostname(hostname: String): MinecraftPingOptions {
        this.hostname = hostname
        return this
    }

    fun setPort(port: Int): MinecraftPingOptions {
        this.port = port
        return this
    }

    fun setTimeout(timeout: Int): MinecraftPingOptions {
        this.timeout = timeout
        return this
    }

    fun setCharset(charset: String): MinecraftPingOptions {
        this.charset = charset
        return this
    }

    fun getHostname(): String {
        return this.hostname!!
    }

    fun getPort(): Int {
        return this.port
    }

    fun getTimeout(): Int {
        return this.timeout
    }

    fun getCharset(): String {
        return this.charset
    }

}