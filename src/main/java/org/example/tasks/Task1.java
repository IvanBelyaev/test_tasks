package org.example.tasks;

public class Task1 {

    /**
     * Метод заменяет все вхождения символа в строке на заданный символ.
     * @param str исходная строка.
     * @param oldChar заменяемый символ.
     * @param newChar новый символ.
     * @return строка с замененными символами.
     */
    public static String replace(String str, final char oldChar, final char newChar) {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch == oldChar) {
                ch = newChar;
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
