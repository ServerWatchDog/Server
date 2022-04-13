package i.server.modules.monitor.service

import i.server.modules.monitor.model.MonitorTypeResultView
import i.server.modules.monitor.model.MonitorTypeView
import i.server.utils.template.crud.CRUDService

interface IMonitorTypeService : CRUDService<MonitorTypeView, MonitorTypeResultView, String>
