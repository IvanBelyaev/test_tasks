package org.example;

import org.example.tasks.Task2;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Task2Test {
    /**
     * Тест для метода parseInt().
     */
    @Test
    public void whenParseIntThenMethodReturnsInt() {
        String str = "123";
        int methodReturns = Task2.parseInt(str);
        assertThat(methodReturns, is(123));
    }

    /**
     * Тест для метода parseDouble().
     */
    @Test
    public void whenParseDoubleThenMethodReturnsDouble() {
        String str = "0.123";
        double methodReturns = Task2.parseDouble(str);
        assertThat(methodReturns, is(0.123));
    }
}
