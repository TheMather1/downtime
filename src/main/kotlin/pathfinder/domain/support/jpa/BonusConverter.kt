package pathfinder.domain.support.jpa

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import pathfinder.domain.character.stats.Bonus
import pathfinder.domain.character.stats.BonusSet
import pathfinder.domain.character.stats.BonusType


@Converter(autoApply = true)
class BonusConverter : AttributeConverter<BonusSet, String> {
    val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: BonusSet?): String? = attribute?.run { objectMapper.writeValueAsString(bonuses) }

    override fun convertToEntityAttribute(dbData: String?): BonusSet = BonusSet(objectMapper.readValue<MutableList<Bonus<BonusType>>>(
        dbData?.replace("[[", "[")?.replace("]]", "]") ?: "[]")
    )
}
