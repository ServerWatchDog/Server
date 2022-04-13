package i.server.modules.client.service

import i.server.modules.client.model.ClientGroupResultView
import i.server.modules.client.model.ClientGroupView
import i.server.utils.template.crud.CRUDService

interface IClientGroupService : CRUDService<ClientGroupView, ClientGroupResultView, Int>
