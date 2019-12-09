package example.phonebook.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ValidationTest {

    @Test
    void requireNullTrue() {
        Object object = null;
        boolean result = Validation.requireNull(object);
        assertTrue(result);
    }

    @Test
    void requireNullFail() {
        Object object = new Object();
        boolean result = Validation.requireNull(object);
        assertFalse(result);
    }

    @Test
    void requireNonNullTrue() {
        Object object = new Object();
        boolean result = Validation.requireNonNull(object);
        assertTrue(result);
    }

    @Test
    void requireNonNullTrueFail() {
        Object object = null;
        boolean result = Validation.requireNonNull(object);
        assertFalse(result);
    }
}