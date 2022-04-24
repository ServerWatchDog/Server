package i.server.modules.config.repositories

import i.server.modules.config.models.ConfigUpdateView
import i.server.modules.config.models.SoftConfigTable
import i.server.modules.config.models.TableConfigView
import i.server.utils.BadRequestException
import org.d7z.light.db.modules.cache.LightCache
import org.d7z.light.db.modules.cache.utils.cacheContext
import org.d7z.light.db.modules.cache.utils.cacheWriteContext
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Transactional
@Repository
class ConfigRepository(
    cache: LightCache,
) {
    private val singleCache = cache.singleCacheGroup("soft-config", String::class, TableConfigView::class)

    fun getValue(key: String): TableConfigView {
        return getOptionalValue(key).get()
    }

    fun getOptionalValue(key: String): Optional<TableConfigView> {
        return singleCache.cacheContext(key) {
            SoftConfigTable.select {
                SoftConfigTable.id eq key
            }.singleOrNull()?.let {
                TableConfigView(
                    it[SoftConfigTable.id].value,
                    it[SoftConfigTable.value],
                    it[SoftConfigTable.defaultValue],
                    it[SoftConfigTable.description],
                    it[SoftConfigTable.internalData],
                    it[SoftConfigTable.type],
                )
            }
        }.executeOptional()
    }

    fun setValue(
        data: TableConfigView
    ) {
        singleCache.cacheWriteContext(data.key) {
            SoftConfigTable.deleteWhere { SoftConfigTable.id eq data.key }
            SoftConfigTable.insert {
                it[id] = data.key
                it[value] = data.value
                it[description] = data.description
                it[defaultValue] = data.defaultValue
                it[internalData] = data.isInternalData
                it[type] = data.type
            }
        }
    }

    fun getAll(): List<TableConfigView> {
        return SoftConfigTable.selectAll().map {
            TableConfigView(
                it[SoftConfigTable.id].value,
                it[SoftConfigTable.value],
                it[SoftConfigTable.defaultValue],
                it[SoftConfigTable.description],
                it[SoftConfigTable.internalData],
                it[SoftConfigTable.type],
            )
        }
    }

    fun updateValue(dataList: List<ConfigUpdateView>) {
        val toMap = dataList.associate { it.key to it.value }
        SoftConfigTable.select { SoftConfigTable.id inList toMap.keys }.map {
            val newData = toMap[it[SoftConfigTable.id].value]!!
            if (it[SoftConfigTable.type].check(newData).not()) {
                throw BadRequestException("配置 ${it[SoftConfigTable.id]} 的内容 '$newData' 不是 ${it[SoftConfigTable.type]} 类型. ")
            }
            it[SoftConfigTable.id].value
        }.forEach { data ->
            SoftConfigTable.update({
                SoftConfigTable.id eq data
            }) {
                it[value] = toMap[data]!!
            }
        }
    }
}
