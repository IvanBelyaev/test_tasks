package org.example.tasks;

public class Task3 {
    /**
     * Программа выводит на экран числа от 1 до 100 включительно.
     * При этом вместо чисел, кратных трем, программа выводит слово Fizz,
     * а вместо чисел, кратных пяти — слово Buzz.
     * Если число кратно и трем, и пяти, то программа выводит слово FizzBuzz.
     * @param args - не используются.
     */
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}
