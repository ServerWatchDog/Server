package i.server.modules

import org.d7z.light.db.api.LightDB
import org.d7z.light.db.modules.cache.LightCache
import org.d7z.light.db.modules.session.LightSession
import org.d7z.objects.format.ObjectFormatContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LightDBConfiguration {
    @Bean
    fun session(lightDB: LightDB, objectFormatContext: ObjectFormatContext) =
        LightSession.Builder(lightDB).apply {
            this.dataCovert = objectFormatContext
        }
    @Bean
    fun cache(lightDB: LightDB) =
        LightCache(lightDB)
}
