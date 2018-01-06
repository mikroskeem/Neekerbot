/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package ch.jamiete.mcping

import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import com.google.gson.Gson
import java.nio.charset.Charset

class MinecraftPing {

    /**
     * Fetches a [MinecraftPingReply] for the supplied hostname.
     * **Assumed timeout of 2s and port of 25565.**
     *
     * @param hostname - a valid String hostname
     * @return [MinecraftPingReply]
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getPing(hostname: String): MinecraftPingReply {
        return this.getPing(MinecraftPingOptions().setHostname(hostname))
    }

    /**
     * Fetches a [MinecraftPingReply] for the supplied options.
     *
     * @param options - a filled instance of [MinecraftPingOptions]
     * @return [MinecraftPingReply]
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getPing(options: MinecraftPingOptions): MinecraftPingReply {
        MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.")
        MinecraftPingUtil.validate(options.getPort(), "Port cannot be null.")

        val socket = Socket()
        socket.connect(InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout())

        val `in` = DataInputStream(socket.getInputStream())
        val out = DataOutputStream(socket.getOutputStream())

        //> Handshake

        val handshake_bytes = ByteArrayOutputStream()
        val handshake = DataOutputStream(handshake_bytes)

        handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE.toInt())
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION)
        MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length)
        handshake.writeBytes(options.getHostname())
        handshake.writeShort(options.getPort())
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE)

        MinecraftPingUtil.writeVarInt(out, handshake_bytes.size())
        out.write(handshake_bytes.toByteArray())

        //> Status request

        out.writeByte(0x01) // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST.toInt())

        //< Status response

        MinecraftPingUtil.readVarInt(`in`) // Size
        var id = MinecraftPingUtil.readVarInt(`in`)

        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.")
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST.toInt(), "Server returned invalid packet.")

        val length = MinecraftPingUtil.readVarInt(`in`)
        MinecraftPingUtil.io(length == -1, "Server prematurely ended stream.")
        MinecraftPingUtil.io(length == 0, "Server returned unexpected value.")

        val data = ByteArray(length)
        `in`.readFully(data)
        val json = String(data, charset = Charset.forName(options.getCharset()))

        //> Ping

        out.writeByte(0x09) // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_PING.toInt())
        out.writeLong(System.currentTimeMillis())

        //< Ping

        MinecraftPingUtil.readVarInt(`in`) // Size
        id = MinecraftPingUtil.readVarInt(`in`)
        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.")
        MinecraftPingUtil.io(id.toByte() != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.")

        // Close

        handshake.close()
        handshake_bytes.close()
        out.close()
        `in`.close()
        socket.close()

        return Gson().fromJson(json, MinecraftPingReply::class.java)
    }

}