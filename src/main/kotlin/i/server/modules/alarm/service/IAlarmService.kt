package i.server.modules.alarm.service

import i.server.modules.alarm.model.ClientAlarmResultView
import i.server.utils.template.PageView
import i.server.utils.template.SimpleView
import org.springframework.data.domain.Pageable

interface IAlarmService {
    fun getClient(page: Pageable): PageView<ClientAlarmResultView>
    fun updateClientOnes(id: Int, data: List<Int>): SimpleView<Boolean>
    fun updateClientGroup(id: Int, data: List<Int>): SimpleView<Boolean>
}
