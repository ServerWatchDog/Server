package i.server.handler.inject.security

interface IAuthorityService {
    fun verify(sessionId: String, permissions: Array<String>): Boolean
}
