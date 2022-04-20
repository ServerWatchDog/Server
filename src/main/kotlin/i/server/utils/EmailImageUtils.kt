package i.server.utils

import org.d7z.security4k.hash.HashUtils

object EmailImageUtils {
    fun getImageUrl(email: String): String {
        if (email.endsWith("@qq.com") && email.replace("@qq.com", "")
            .contains(Regex("\\d*"))
        ) {
            return "https://q1.qlogo.cn/g?b=qq&nk=${email.replace("@qq.com", "")}&s=100"
        }
        return "https://www.gravatar.com/avatar/${
        HashUtils.loadTextHash(
            email.lowercase(),
            method = HashUtils.METHOD.MD5
        ).lowercase()
        }?s=200"
    }
}
