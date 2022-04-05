package i.server.modules.config.repositories

import i.server.modules.config.models.table.SoftConfigTable
import org.d7z.light.db.modules.cache.LightCache
import org.d7z.light.db.modules.cache.utils.cacheContext
import org.d7z.light.db.modules.cache.utils.cacheWriteContext
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Transactional
@Repository
class ConfigRepository(
    cache: LightCache,
) {
    private val singleCache = cache.singleCacheGroup("soft-config", String::class, String::class)

    fun getValue(key: String): String {
        return getOptionalValue(key).get()
    }

    fun getOptionalValue(key: String): Optional<String> {
        return singleCache.cacheContext(key) {
            SoftConfigTable.select {
                SoftConfigTable.key eq key
            }.singleOrNull()?.get(SoftConfigTable.value)
        }.executeOptional()
    }

    fun setValue(key: String, value: String) {
        singleCache.cacheWriteContext(key) {
            SoftConfigTable.insertIgnore {
                it[SoftConfigTable.key] = key
                it[SoftConfigTable.value] = value
            }
        }
    }
}
