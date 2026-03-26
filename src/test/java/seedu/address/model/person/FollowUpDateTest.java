package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class FollowUpDateTest {

    @Test
    public void constructor_validPastDate_success() {
        FollowUpDate date = new FollowUpDate("2020-01-01");
        assertEquals(LocalDate.of(2020, 1, 1), date.value);
    }
}
