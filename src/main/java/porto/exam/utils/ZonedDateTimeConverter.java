package porto.exam.utils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    @Override
    public String convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.format(FORMATTER) : null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(String dbData) {
        return dbData != null ? ZonedDateTime.parse(dbData, FORMATTER) : null;
    }
}
