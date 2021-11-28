package org.example.tasks;

public class Task2 {
    /**
     * Преобразует строку в число типа int.
     * @param str строка вида +123 или -123 или 123.
     * @return число типа int полученное из строки.
     */
    public static int parseInt(String str) {
        if (str == null) {
            throw new NumberFormatException("string is null");
        }
        if (str.length() == 0) {
            throw new NumberFormatException("string is empty");
        }

        // номер текущего символа в строке
        int i = 0;
        // длина строки
        int length = str.length();
        // максимальный предел, после которого считывать новые символы из строки нет смысла.
        // Если промежуточный результат превысит этот порог, то произойдет переполнение
        int limit = Integer.MAX_VALUE / 10;
        // знак числа
        boolean negative = false;
        // поправка. Модуль Integer.MAX_VALUE меньше модуля Integer.MIN_VALUE на 1.
        // для положительного числа равно 0. для отрицательного равна 1.
        int maxMinLimitDiff = 0;

        // проверка знака числа
        char firstChar = str.charAt(0);
        if (firstChar == '-') {
            negative = true;
            limit = Math.abs(Integer.MIN_VALUE / 10);
            maxMinLimitDiff = 1;
            i++;
        } else if (firstChar == '+') {
            i++;
        }
        if (i == length) {  // если кроме знака ничего нет, то выбрасываем исключение
            throw new NumberFormatException("there is no number only a sign");
        }

        int result = 0;
        while (i < length) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch) && result <= limit) { // проверка переполнения
                result *= 10;
                int digit = Character.digit(ch, 10);
                if (result + digit - maxMinLimitDiff < result) { // проверка переполнения
                    throw new NumberFormatException("overflow");
                }
                result += digit;
            } else {
                throw new NumberFormatException("error near index " + i);
            }
            i++;
        }
        if (negative) {
            result *= -1;
        }
        return result;
    }

    /**
     * Преобразует строку в число типа double.
     * @param str строка вида 1.912 или -1.912 или +1.912.
     * @return число типа double полученное из строки.
     */
    public static double parseDouble(String str) {
        if (str == null) {
            throw new NumberFormatException("string is null");
        }
        if (str.length() == 0) {
            throw new NumberFormatException("string is empty");
        }

        // определение целой и дробной части
        String[] parts = str.split("\\.");
        // мантисса
        String mantissa;
        // порядок числа
        int divisionDegree;
        if (parts.length > 2) {             // если в строке встречается больше одной точки
            throw new NumberFormatException("incorrect number format");
        } else if (parts.length == 2) {     // если есть целай и дробная часть
            mantissa = parts[0] + parts[1];
            divisionDegree = parts[1].length();
        } else {                            // если есть только целая часть
            mantissa = str;
            divisionDegree = 0;
        }

        // номер текущего символа в строке
        int i = 0;
        // знак числа
        boolean negative = false;
        // проверка знака числа
        char firstChar = str.charAt(0);
        if (firstChar == '-') {
            negative = true;
            i++;
        } else if (firstChar == '+') {
            i++;
        }
        // если есть только знак, но нет числа - выбрасываем исключение
        if (i == mantissa.length()) {
            throw new NumberFormatException("there is no number only a sign");
        }
        double result = 0;
        for ( ; i < mantissa.length(); i++) {
            char ch = mantissa.charAt(i);
            if (Character.isDigit(ch)) {
                int digit = Character.digit(ch, 10);
                result = result * 10 + digit;
            } else {
                throw new NumberFormatException("incorrect number format");
            }
        }
        result /= Math.pow(10, divisionDegree);
        if (negative) {
            result *= -1;
        }
        return result;
    }
}
