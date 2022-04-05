package i.server.modules.user.service

import i.server.modules.user.model.view.RoleResultView
import i.server.modules.user.model.view.RoleView
import i.server.utils.template.crud.CRUDService

interface IRoleService : CRUDService<RoleView, RoleResultView, Int>
