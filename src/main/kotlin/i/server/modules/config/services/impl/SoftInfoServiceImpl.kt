package i.server.modules.config.services.impl

import i.server.handler.inject.encrypt.EncryptView
import i.server.modules.config.repositories.ConfigRepository
import i.server.modules.config.services.SoftInfoService
import org.d7z.security4k.api.IDataEncrypt
import org.d7z.security4k.rsa.RSAKeyGenerator
import org.d7z.security4k.rsa.RSAPrivate
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service

@Service
class SoftInfoServiceImpl(
    private val configRepository: ConfigRepository,
) : SoftInfoService, ApplicationRunner {

    override fun getPrivateKey(type: EncryptView.EncryptType): IDataEncrypt {
        return RSAPrivate(configRepository.getValue("security.rsa.private-key"))
    }

    override fun run(args: ApplicationArguments?) {
        if (configRepository.getOptionalValue("security.rsa.public-key").isEmpty) {
            val rsaKeyGenerator = RSAKeyGenerator(4096)
            configRepository.setValue("security.rsa.public-key", rsaKeyGenerator.publicKeyText)
            configRepository.setValue("security.rsa.private-key", rsaKeyGenerator.privateKeyText)
        }
    }
}
