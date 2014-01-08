package fr.oltruong.teamag.entity.converter;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Olivier Truong
 */
public class DateConverterTest {

    private DateConverter dateConverter;

    @Before
    public void setUp() {
        dateConverter = new DateConverter();
    }

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        DateTime dateTime = DateTime.now();
        assertThat(dateConverter.convertToDatabaseColumn(dateTime)).isEqualTo(dateTime.withTimeAtStartOfDay().toDate());
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        DateTime dateTime = DateTime.now();
        assertThat(dateConverter.convertToEntityAttribute(dateTime.toDate())).isEqualTo(dateTime);
    }
}
