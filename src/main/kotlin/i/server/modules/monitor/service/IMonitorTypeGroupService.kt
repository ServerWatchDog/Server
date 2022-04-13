package i.server.modules.monitor.service

import i.server.modules.monitor.model.MonitorTypeGroupResultView
import i.server.modules.monitor.model.MonitorTypeGroupView
import i.server.utils.template.crud.CRUDService

interface IMonitorTypeGroupService : CRUDService<MonitorTypeGroupView, MonitorTypeGroupResultView, Int>
