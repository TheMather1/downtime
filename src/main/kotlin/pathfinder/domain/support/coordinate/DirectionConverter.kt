package pathfinder.domain.support.coordinate

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import pathfinder.domain.support.direction.Cardinal
import pathfinder.domain.support.direction.Diagonal
import pathfinder.domain.support.direction.Direction

@Converter
class DirectionConverter: AttributeConverter<Direction<*>, String> {
    override fun convertToDatabaseColumn(attribute: Direction<*>?) = attribute?.toString()

    override fun convertToEntityAttribute(dbData: String?): Direction<*>? = when {
         dbData.isNullOrBlank() -> null
         dbData in cardinals -> Cardinal.valueOf(dbData)
         else -> Diagonal.valueOf(dbData)
     }

    companion object {
        val cardinals = Cardinal.entries.map { it.name }
    }
}