package i.server.modules.user.service.impl

import i.server.modules.user.model.PermissionsResultView
import i.server.modules.user.model.PermissionsTable
import i.server.modules.user.service.IPermissionsService
import i.server.utils.autoRollback
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.AutoConfigurationPackages
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

@Service
class PermissionsServiceImpl(
    private val applicationContext: ApplicationContext,
) : IPermissionsService, ApplicationRunner {

    @Suppress("UNCHECKED_CAST")
    override fun run(args: ApplicationArguments?) {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AssignableTypeFilter(UserAuthority::class.java))
        val packages = AutoConfigurationPackages.get(applicationContext)
        val components = packages.map { provider.findCandidateComponents(it) }.flatten()
        components.map { Class.forName(it.beanClassName).kotlin.objectInstance as UserAuthority }
            .forEach { data ->
                data::class.memberProperties.forEach { type ->
                    autoRollback {
                        PermissionsTable.insertIgnore { insert ->
                            insert[id] = type.name
                            insert[description] = if (type.hasAnnotation<UserAuthorityType>()) {
                                type.findAnnotation<UserAuthorityType>()!!.description
                            } else {
                                type.call().toString()
                            }
                        }
                    }
                }
            }
    }

    interface UserAuthority
    annotation class UserAuthorityType(
        val description: String,
    )

    @Transactional
    override fun getPermissions(): List<PermissionsResultView> {
        return PermissionsTable.selectAll().map {
            PermissionsResultView(
                it[PermissionsTable.id].value,
                it[PermissionsTable.description]
            )
        }
    }
}
