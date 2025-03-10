package pathfinder.domain.support.coordinate

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class CoordinateConverter: AttributeConverter<Coordinate, String> {
    override fun convertToDatabaseColumn(attribute: Coordinate?) = attribute?.toString()

    override fun convertToEntityAttribute(dbData: String?)
     = if (dbData.isNullOrBlank()) null else Coordinate(dbData)
}