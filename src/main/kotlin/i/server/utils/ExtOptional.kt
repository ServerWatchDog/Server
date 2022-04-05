package i.server.utils

import java.util.Optional

inline fun <reified T : Any> T?.optionalWrapper(): Optional<T> {
    return Optional.ofNullable(this)
}
