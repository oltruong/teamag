package fr.oltruong.teamag.entity.converter;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Date;

@Converter
public class DateConverter implements AttributeConverter<DateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(DateTime dateTime) {
        return dateTime.withTimeAtStartOfDay().toDate();
    }

    @Override
    public DateTime convertToEntityAttribute(Date date) {
        return new DateTime(date);
    }

}
