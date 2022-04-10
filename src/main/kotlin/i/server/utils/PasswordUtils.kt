package i.server.utils

import org.d7z.security4k.hash.HashUtils

object PasswordUtils {
    fun hash(plain: String): String {
        var result = plain
        for (i in 0 until 10) {
            if (i % 2 == 0) {
                result = HashUtils.loadTextHash("slat-$plain", method = HashUtils.METHOD.SHA256)
            } else {
                result = HashUtils.loadTextHash("$plain-slat", method = HashUtils.METHOD.SHA256)
            }
        }
        return result
    }
}
