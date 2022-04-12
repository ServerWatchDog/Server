package i.server.modules.client.service

import i.server.handler.inject.security.IAuthorityService

interface ClientSessionService : IAuthorityService {
    fun generateToken(): String
}
