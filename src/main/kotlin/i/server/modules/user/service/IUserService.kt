package i.server.modules.user.service

import i.server.modules.user.model.view.UserResultView
import i.server.modules.user.model.view.UserView
import i.server.utils.template.crud.CRUDService

interface IUserService : CRUDService<UserView, UserResultView, Int>
