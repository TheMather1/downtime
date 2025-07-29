package pathfinder.domain.support.jpa

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.awt.Color


@Converter(autoApply = true)
class ColorConverter : AttributeConverter<Color, String> {
    override fun convertToDatabaseColumn(attribute: Color) =
        "#" + Integer.toHexString(attribute.rgb and 0xffffff)

    override fun convertToEntityAttribute(dbData: String): Color = Color.decode(dbData)
}
