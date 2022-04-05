package i.server.modules.config.services

import i.server.handler.inject.encrypt.EncryptView
import org.d7z.security4k.api.IDataEncrypt

interface SoftInfoService {
    fun getPrivateKey(type: EncryptView.EncryptType): IDataEncrypt
}
