package i.server.modules.log.service.impl

import i.server.modules.client.model.ClientTable
import i.server.modules.config.services.SoftInfoService
import i.server.modules.log.model.ClientLogItemTable
import i.server.modules.log.model.ClientLogTable
import i.server.modules.log.service.ILogCollectionService
import i.server.modules.monitor.model.MonitorType
import i.server.modules.monitor.service.IClientMonitorTypeService
import i.server.utils.autoRollback
import jakarta.annotation.PreDestroy
import org.d7z.light.db.api.LightDB
import org.d7z.logger4k.core.utils.getLogger
import org.d7z.objects.format.api.IDataCovert
import org.d7z.objects.format.utils.reduce
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask

@Service
class LogCollectionServiceImpl(
    private val softInfoService: SoftInfoService,
    private val clientMonitorService: IClientMonitorTypeService,
    lightDB: LightDB,
    private val dataCovert: IDataCovert,
) : ILogCollectionService, ApplicationRunner, TimerTask() {
    private val clients = lightDB.withMap("client-monitor")

    private val timer = Timer()
    private val logger = getLogger()
    override fun run() = autoRollback {
        logger.info("定时日志采集.")
        ClientTable.select { ClientTable.enable eq true }.filter {
            clients.get("client:${it[ClientTable.id].value}", String::class, String::class)
                .filter { it1 ->
                    it1["push_time"].filter { data ->
                        dataCovert.reduce(data, LocalDateTime::class)
                            .isAfter(LocalDateTime.now().minusSeconds(softInfoService.logRefreshDate))
                    }.isPresent
                }.isPresent
        }.map { it[ClientTable.id].value }.forEach { clientId ->
            clients.get("client:$clientId", String::class, String::class).ifPresent { db ->
                val logItemId = ClientLogTable.insertAndGetId {
                    it[client] = clientId
                    it[createTime] = LocalDateTime.now()
                }.value
                db["client-monitor"]
                    .map { d -> dataCovert.reduce<Map<String, MonitorType>>(d) }
                    .orElseGet { clientMonitorService.getClientMonitor(clientId) }
                    .forEach { id ->
                        db["monitor.${id.key}"].ifPresent { value ->
                            ClientLogItemTable.insert {
                                it[clientLog] = logItemId
                                it[type] = id.key
                                it[ClientLogItemTable.value] = value
                            }
                        }
                    }
            }
        }
    }

    override fun run(args: ApplicationArguments?) {
        timer.schedule(this, 0L, softInfoService.logRefreshDate * 1000)
    }

    @PreDestroy
    fun destroy() {
        timer.cancel()
    }
}
