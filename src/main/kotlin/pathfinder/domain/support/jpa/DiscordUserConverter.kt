package pathfinder.domain.support.jpa

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Converter
@Component
class DiscordUserConverter: AttributeConverter<User, Long> {
    @Autowired
    fun setJDA(jda: JDA) {
        DiscordUserConverter.jda = jda
    }

    override fun convertToDatabaseColumn(attribute: User?) = attribute?.idLong

    override fun convertToEntityAttribute(dbData: Long?): User? = dbData?.let {
        jda.getUserById(it) ?: jda.retrieveUserById(it).complete()
    }

    companion object {
        lateinit var jda: JDA
    }
}
