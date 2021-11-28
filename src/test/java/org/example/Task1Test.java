package org.example;

import org.example.tasks.Task1;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Task1Test {
    /**
     * Тест для метода replace().
     */
    @Test
    public void whenReplaceThenMethodReturnsStringWithReplacedCharacters() {
        String str = "Hello";
        String methodReturns = Task1.replace(str, 'l', 'k');
        assertThat(methodReturns, is("Hekko"));
    }
}
