package i.server.handler.inject.encrypt

data class EncryptView(
    val type: EncryptType,
    val cipher: String
) {
    enum class EncryptType {
        RSA
    }
}
