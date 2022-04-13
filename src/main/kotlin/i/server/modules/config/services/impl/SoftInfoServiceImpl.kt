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

    override var agentRefreshDate: Long
        get() = configRepository.getOptionalValue("agent.refresh.time").orElse("15").toLong()
        set(value) {
            configRepository.setValue("agent.refresh.time", value.toString())
        }
    override var logRefreshDate: Long
        get() = configRepository.getOptionalValue("log.refresh.time").orElse("300").toLong()
        set(value) {
            configRepository.setValue("log.refresh.time", value.toString())
        }

    override fun getPrivateKey(type: EncryptView.EncryptType): IDataEncrypt {
        return RSAPrivate(configRepository.getValue("security.rsa.private-key"))
    }

    override fun run(args: ApplicationArguments?) {
        if (configRepository.getOptionalValue("security.rsa.public-key").isEmpty ||
            configRepository.getOptionalValue("security.rsa.private-key").isEmpty
        ) {
            val rsaKeyGenerator = RSAKeyGenerator(4096)
            configRepository.setValue("security.rsa.public-key", rsaKeyGenerator.publicKeyText)
            configRepository.setValue("security.rsa.private-key", rsaKeyGenerator.privateKeyText)
        }
    }
}
