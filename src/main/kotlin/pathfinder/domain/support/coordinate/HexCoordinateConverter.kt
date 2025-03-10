package pathfinder.domain.support.coordinate

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class HexCoordinateConverter: AttributeConverter<HexCoordinate, String> {
    override fun convertToDatabaseColumn(attribute: HexCoordinate?) = attribute?.axialKey

    override fun convertToEntityAttribute(dbData: String?)
     = if (dbData.isNullOrBlank()) null else HexCoordinate(dbData)
}