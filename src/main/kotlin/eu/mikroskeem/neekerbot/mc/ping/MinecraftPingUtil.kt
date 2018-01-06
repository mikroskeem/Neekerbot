/*
 * ALL RIGHTS RESERVED AT THE MOMENT. WHEN LICENSE IS CHANGED, NEW LICENSE REPLACES THIS LICENSE HERE.
 */

package ch.jamiete.mcping

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

object MinecraftPingUtil {

    var PACKET_HANDSHAKE: Byte = 0x00
    var PACKET_STATUSREQUEST: Byte = 0x00
    var PACKET_PING: Byte = 0x01
    var PROTOCOL_VERSION = 4
    var STATUS_HANDSHAKE = 1

    fun validate(o: Any?, m: String) {
        if(o == null) {
            throw RuntimeException(m)
        }
    }

    @Throws(IOException::class)
    fun io(b: Boolean, m: String) {
        if(b) {
            throw IOException(m)
        }
    }

    /**
     * @author thinkofdeath
     * See: https://gist.github.com/thinkofdeath/e975ddee04e9c87faf22
     */
    @Throws(IOException::class)
    fun readVarInt(`in`: DataInputStream): Int {
        var i = 0
        var j = 0
        while(true) {
            val k = `in`.readByte().toInt()

            i = i or (k and 0x7F shl j++ * 7)

            if(j > 5)
                throw RuntimeException("VarInt too big")

            if(k and 0x80 != 128)
                break
        }

        return i
    }

    /**
     * @author thinkofdeath
     * See: https://gist.github.com/thinkofdeath/e975ddee04e9c87faf22
     * @throws IOException
     */
    @Throws(IOException::class)
    fun writeVarInt(out: DataOutputStream, paramInt: Int) {
        var paramInt = paramInt
        while(true) {
            if(paramInt and -0x80 == 0) {
                out.writeByte(paramInt)
                return
            }

            out.writeByte(paramInt and 0x7F or 0x80)
            paramInt = paramInt ushr 7
        }
    }

}