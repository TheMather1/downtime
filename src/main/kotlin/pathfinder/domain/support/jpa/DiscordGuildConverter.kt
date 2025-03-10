package pathfinder.domain.support.jpa

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Converter
@Component
class DiscordGuildConverter: AttributeConverter<Guild, Long> {
    @Autowired
    fun setJDA(jda: JDA) {
        DiscordGuildConverter.jda = jda
    }

    override fun convertToDatabaseColumn(attribute: Guild?) = attribute?.idLong

    override fun convertToEntityAttribute(dbData: Long?): Guild? = dbData?.let(jda::getGuildById)

    companion object {
        lateinit var jda: JDA
    }
}