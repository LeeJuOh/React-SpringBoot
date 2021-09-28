package com.example.gccoffeeclone.order.model;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("abcdef");
        });
    }

    @Test
    void testSizeInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("aaa");
        });
    }

    @Test
    void testValidEmail() {
        var email = new Email("hello@gmail.com");
        assertThat(email.getAddress(), is("hello@gmail.com"));
    }

    @Test
    void testEqEmail() {
        var email = new Email("hello@gmail.com");
        var email2 = new Email("hello@gmail.com");
        assertThat(email, is(email2));
    }

}