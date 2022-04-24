package i.server.modules.config.services.impl

import i.server.handler.inject.encrypt.EncryptView
import i.server.modules.config.models.ConfigUpdateView
import i.server.modules.config.models.ConfigView
import i.server.modules.config.models.TableConfigView
import i.server.modules.config.repositories.ConfigRepository
import i.server.modules.config.services.SoftInfoService
import i.server.utils.interpreter.RuleDataType
import i.server.utils.template.SimpleView
import org.d7z.objects.format.api.IDataCovert
import org.d7z.security4k.api.IDataEncrypt
import org.d7z.security4k.rsa.RSAKeyGenerator
import org.d7z.security4k.rsa.RSAPrivate
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@Lazy
@Service
class SoftInfoServiceImpl(
    private val configRepository: ConfigRepository,
    private val dataCovert: IDataCovert,
) : SoftInfoService, ApplicationRunner {

    private val initFun: ArrayList<() -> Unit> = ArrayList(128)

    override var agentRefreshDate: Long by globalConfig(
        "agent.refresh.time", "客户端上报间隔", Long::class, 15, false, RuleDataType.NUMBER
    )

    override var logRefreshDate: Long by globalConfig(
        "log.refresh.time", "日志采集间隔时间", Long::class, 300, false, RuleDataType.NUMBER
    )
    override var smtpHost: String by globalConfig(
        "smtp.host", "SMTP 服务器地址", String::class, "example.com", false, RuleDataType.TEXT
    )
    override var smtpPort: Int by globalConfig(
        "smtp.port", "SMTP 服务器端口", Int::class, 139, false, RuleDataType.NUMBER
    )
    override var smtpEmail: String by globalConfig(
        "smtp.mail", "SMTP 服务器邮箱", String::class, "admin@example.com", false, RuleDataType.TEXT
    )
    override var smtpUser: String by globalConfig(
        "smtp.user", "SMTP 服务器用户名", String::class, "admin", false, RuleDataType.TEXT
    )
    override var smtpPassword: String by globalConfig(
        "smtp.password", "SMTP 服务器密码", String::class, "admin", false, RuleDataType.TEXT
    )

    var rsaPublicKey by globalConfig("security.rsa.public-key", "", String::class, "")

    var rsaPrivateKey by globalConfig("security.rsa.private-key", "", String::class, "")
    override fun getPrivateKey(type: EncryptView.EncryptType): IDataEncrypt {
        return RSAPrivate(rsaPrivateKey)
    }

    override fun run(args: ApplicationArguments?) {
        initFun.forEach { it -> it() }
        if (rsaPublicKey.isEmpty() || rsaPrivateKey.isEmpty()) {
            val rsaKeyGenerator = RSAKeyGenerator(4096)
            rsaPublicKey = rsaKeyGenerator.publicKeyText
            rsaPrivateKey = rsaKeyGenerator.privateKeyText
        }
    }

    final override fun <T : Any> globalConfig(
        key: String,
        description: String,
        jvmType: KClass<T>,
        default: T,
        privateData: Boolean,
        type: RuleDataType
    ): ReadWriteProperty<Any?, T> {
        return object : ReadWriteProperty<Any?, T> {
            init {
                initFun.add {
                    if (configRepository.getOptionalValue(key).isEmpty) {
                        configRepository.setValue(
                            TableConfigView(
                                key, dataCovert.format(default, jvmType),
                                dataCovert.format(default, jvmType), description, privateData, type
                            )
                        )
                    }
                }
            }

            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return configRepository.getOptionalValue(key).map {
                    dataCovert.reduce(it.value, jvmType)
                }.orElseGet {
                    configRepository.setValue(
                        TableConfigView(
                            key, dataCovert.format(default, jvmType),
                            dataCovert.format(default, jvmType), description,
                            privateData, type
                        )
                    )
                    default
                }
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                configRepository.setValue(
                    TableConfigView(
                        key,
                        dataCovert.format(value, jvmType),
                        dataCovert.format(default, jvmType),
                        description,
                        privateData,
                        type
                    )
                )
            }
        }
    }

    override fun getAllConfig(): List<ConfigView> {
        return configRepository.getAll().filter { it.isInternalData.not() }.map {
            ConfigView(it.key, it.value, it.description, it.defaultValue, it.type)
        }
    }

    override fun updateConfig(data: List<ConfigUpdateView>): SimpleView<Boolean> {
        configRepository.updateValue(data)
        return SimpleView(true)
    }
}
