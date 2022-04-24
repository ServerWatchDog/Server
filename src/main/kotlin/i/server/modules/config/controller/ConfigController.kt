package i.server.modules.config.controller

import i.server.handler.inject.security.Permission
import i.server.modules.config.ConfigAuthority
import i.server.modules.config.models.ConfigUpdateView
import i.server.modules.config.services.SoftInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/configs")
@Permission("user", [ConfigAuthority.CONFIG_ADMIN])
class ConfigController(
    private val configService: SoftInfoService
) {
    @Permission("user", [ConfigAuthority.CONFIG_ADMIN])
    @GetMapping("")
    fun getConfig() = configService.getAllConfig()

    @Permission("user", [ConfigAuthority.CONFIG_ADMIN])
    @PostMapping("")
    fun updateConfig(@RequestBody data: List<ConfigUpdateView>) = configService.updateConfig(data)
}
