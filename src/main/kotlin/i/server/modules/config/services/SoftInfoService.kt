package i.server.modules.config.services

import i.server.handler.inject.encrypt.EncryptView
import i.server.modules.config.models.ConfigUpdateView
import i.server.modules.config.models.ConfigView
import i.server.utils.interpreter.RuleDataType
import i.server.utils.template.SimpleView
import org.d7z.security4k.api.IDataEncrypt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

interface SoftInfoService {
    var agentRefreshDate: Long

    var logRefreshDate: Long

    var smtpHost: String
    var smtpPort: Int
    var smtpEmail: String
    var smtpUser: String
    var smtpPassword: String

    fun <T : Any> globalConfig(
        key: String,
        description: String,
        jvmType: KClass<T>,
        default: T,
        privateData: Boolean = true,
        type: RuleDataType = RuleDataType.TEXT
    ): ReadWriteProperty<Any?, T>

    fun getPrivateKey(type: EncryptView.EncryptType): IDataEncrypt
    fun getAllConfig(): List<ConfigView>
    fun updateConfig(data: List<ConfigUpdateView>): SimpleView<Boolean>
}
