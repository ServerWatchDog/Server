package i.server.modules.alarm.controller

import i.server.handler.inject.page.RestPage
import i.server.handler.inject.security.Permission
import i.server.modules.alarm.AlarmAuthority
import i.server.modules.alarm.service.IAlarmService
import i.server.modules.client.ClientAuthority
import i.server.utils.template.SimpleView
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [AlarmAuthority.ALARM_ADMIN, ClientAuthority.CLIENT_ADMIN])
@RestController
@RequestMapping("/api/admin/alarm/rule-clients")
class AlarmController(private val alarm: IAlarmService) {
    @GetMapping("")
    fun getClient(@RestPage page: Pageable) = alarm.getClient(page)

    @PutMapping("/{id}/by/ones")
    fun updateClientOnes(@PathVariable id: Int, @RequestBody data: List<Int>): SimpleView<Boolean> {
        return alarm.updateClientOnes(id, data)
    }

    @PutMapping("/{id}/by/group")
    fun updateClientGroup(@PathVariable id: Int, @RequestBody data: List<Int>): SimpleView<Boolean> {
        return alarm.updateClientGroup(id, data)
    }
}
