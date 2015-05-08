package com.oltruong.teamag.model.converter;

import java.time.LocalDate;
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
        LocalDate dateTime = LocalDate.now();
        assertThat(dateConverter.convertToDatabaseColumn(dateTime)).isEqualTo(dateTime.withTimeAtStartOfDay().toDate());
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        LocalDate dateTime = LocalDate.now();
        assertThat(dateConverter.convertToEntityAttribute(dateTime.toDate())).isEqualTo(dateTime);
    }
}
