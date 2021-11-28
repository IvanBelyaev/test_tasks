package org.example.tasks.task4;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Task4 {
    /**
     * Параметр для внешней сортировки.
     * Количество строк сортируемых в оперативной памяти и записываемых в один временный файл.
     */
    private static final int MAX_LINE_PER_TMP_FILE = 3;

    /**
     * Формат даты в CSV файле.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");

    /**
     * Метод сортирует небольшие CSV файлы, которые целиком помещаются в оперативную память.
     * @param file адрес файла.
     * @throws IOException - исключения ввода / вывода.
     */
    public void sortingSCVFileInMemory(String file) throws IOException {
        // Считываем файл, конвертирум каждую строку в объект и сохраняем в List.
        List<Data> dataList = Files.readAllLines(Path.of(file)).stream()
                .skip(1)
                .map(this::convertStringToData)
                .collect(Collectors.toList());
        // Сортируем объекты по полю fid.
        Collections.sort(dataList);
        // Записываем отсортированные объекты обратно в файл, предварительно преобразовав их в строки.
        try(PrintWriter pw = new PrintWriter(file)) {
            // зоголовок файла
            pw.println("FID;SERIAL_NUM;MEMBER_CODE;ACCT_TYPE;OPENED_DT;ACCT_RTE_CDE;REPORTING_DT;CREDIT_LIMIT");
            dataList.stream()
                    .map(this::convertDataToString)
                    .forEach(pw::println);
        }
    }

    /**
     * Метод использует внешнюю сортировку для сортировки CSV файл.
     * @param file адрес файла.
     * @throws IOException исключения ввода / вывода.
     */
    public void externalSortingOfSCVFile(String file) throws IOException {
        // Разбиваем исходный файл на часли. Каждую часть сортируем и сохраняем во временный файл.
        List<File> tmpFiles = sortFileByPartsIntoTemporaryFiles(file);
        // рекурсивно производим слияние всех временных файлов в один временный файл
        File tmpFileWithAllData = mergeAllTmpFiles(tmpFiles);
        // копируем содержимое временного файла в первоначальный файл с заменой
        try (PrintWriter pw = new PrintWriter(file);
             BufferedReader br = new BufferedReader(new FileReader(tmpFileWithAllData))) {
            // заголовок файла
            pw.println("FID;SERIAL_NUM;MEMBER_CODE;ACCT_TYPE;OPENED_DT;ACCT_RTE_CDE;REPORTING_DT;CREDIT_LIMIT");
            String line;
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
        }
    }

    /**
     * Метод читает из файла по MAX_LINE_PER_TMP_FILE строк, сортирует их и записывает во временный файл.
     * И так продолжает пока не закончатся записи в основном файле.
     * @param file адрес файла.
     * @return список временных файлов, которые содержат части данных основного файла в отсортированном виде.
     * @throws IOException исключения ввода / вывода.
     */
    private List<File> sortFileByPartsIntoTemporaryFiles(String file) throws IOException {
        List<File> tmpFiles = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(file))) {
            String line = br.readLine(); // пропускаем заголовок
            List<Data> dataList = new ArrayList<>();
            while (((line = br.readLine()) != null)) {
                Data data = convertStringToData(line);
                dataList.add(data);
                if (dataList.size() == MAX_LINE_PER_TMP_FILE) {
                    Collections.sort(dataList);
                    File tempFile = writeDataListToTmpFile(dataList);
                    tmpFiles.add(tempFile);
                    dataList.clear();
                }
            }
            if (!dataList.isEmpty()) {
                Collections.sort(dataList);
                File tempFile = writeDataListToTmpFile(dataList);
                tmpFiles.add(tempFile);
            }
        }
        return tmpFiles;
    }

    /**
     * Метод записывает список объектов во временный файл.
     * @param dataList список объектов.
     * @return адрес временного файла.
     * @throws IOException исключения ввода / вывода.
     */
    private File writeDataListToTmpFile(List<Data> dataList) throws IOException {
        File tempFile = File.createTempFile("tmp", ".csv");
        tempFile.deleteOnExit();
        try (PrintWriter pw = new PrintWriter(tempFile)) {
            dataList.stream()
                    .map(this::convertDataToString)
                    .forEach(pw::println);
        }
        return tempFile;
    }

    /**
     * Метод объединяет все временные файлы в один.
     * @param tempFiles список всех временных файлов.
     * @return временный файл, который содержит все записи. Данные отсортированы.
     * @throws IOException исключения ввода / вывода.
     */
    private File mergeAllTmpFiles(List<File> tempFiles) throws IOException {
        while (tempFiles.size() > 1) {
            tempFiles = mergeTmpFiles(tempFiles);
        }
        return tempFiles.get(0);
    }

    /**
     * Метод объдиняет временные файлы попарно, создавая новые временные файлы.
     * @param tempFiles временных файлов.
     * @return новый список временных файлов,
     * в котором данные файлов с индексами i и i + 1 объединены и отсортированы.
     * @throws IOException исключения ввода / вывода.
     */
    private List<File> mergeTmpFiles(List<File> tempFiles) throws IOException {
        List<File> newTempFiles = new ArrayList<>();
        for(int i = 0; i + 1 < tempFiles.size(); i += 2) {
            File newTempFile = File.createTempFile("tmp", ".csv");
            newTempFile.deleteOnExit();
            newTempFiles.add(newTempFile);
            try (BufferedReader br1 = Files.newBufferedReader(tempFiles.get(i).toPath());
                 BufferedReader br2 = Files.newBufferedReader(tempFiles.get(i + 1).toPath());
                 PrintWriter pw = new PrintWriter(newTempFile)) {

                String line1 = br1.readLine();
                String line2 = br2.readLine();
                while (line1 != null && line2 != null) {
                    Data data1 = convertStringToData(line1);
                    Data data2 = convertStringToData(line2);
                    if (data1.compareTo(data2) < 0) {
                        pw.println(line1);
                        line1 = br1.readLine();
                    } else {
                        pw.println(line2);
                        line2 = br2.readLine();
                    }
                }

                while (line1 != null) {
                    pw.println(line1);
                    line1 = br1.readLine();
                }

                while (line2 != null) {
                    pw.println(line2);
                    line2 = br2.readLine();
                }
            }
        }
        if (tempFiles.size() % 2 != 0) {
            newTempFiles.add(tempFiles.get(tempFiles.size() - 1));
        }
        return newTempFiles;
    }

    /**
     * Метод преобразует объект в строку для записи в CSV файл.
     * @param data объект для преобразования.
     * @return объект преобразованный в строку.
     */
    private String convertDataToString(Data data) {
        List<String> memberCodes = data.getMemberCodes();
        String memberCodesLikeString;
        if (memberCodes.size() > 1) {
            StringJoiner sj = new StringJoiner(";", "”", "”");
            memberCodes.stream()
                    .forEach(s -> sj.add(s));
            memberCodesLikeString = sj.toString();
        } else {
            memberCodesLikeString = memberCodes.get(0);
        }
        String line = String.join(";",
                Integer.toString(data.getFid()),
                Integer.toString(data.getSerialNum()),
                memberCodesLikeString,
                Integer.toString(data.getAcctType()),
                data.getOpenedDt().format(formatter),
                Integer.toString(data.getAcctRteCde()),
                data.getReportingDt().format(formatter),
                Integer.toString(data.getCreditLimit())
        );
        return line;
    }

    /**
     * Метод преобразует строку CSV файла в объект.
     * @param line строка CSV файла.
     * @return объект полученный из строки.
     */
    private Data convertStringToData(String line) {
        String[] tokens = line.split(";(?=(?:[^”]*”[^”]*”)*[^”]*$)", -1);
        int fid = Integer.parseInt(tokens[0]);
        int serialNum = Integer.parseInt(tokens[1]);
        List<String> memberCodes = List.of(
                tokens[2].replaceAll("”", "").split(";"));
        int acctType = Integer.parseInt(tokens[3]);
        LocalDate openedDt = LocalDate.parse(tokens[4], formatter);
        int acctRteCde = Integer.parseInt(tokens[5]);
        LocalDate reportingDt = LocalDate.parse(tokens[6], formatter);
        int creditLimit = Integer.parseInt(tokens[7]);
        return new Data(fid, serialNum, memberCodes, acctType,
                openedDt, acctRteCde, reportingDt, creditLimit);
    }

    public static void main(String[] args) throws IOException {
        Task4 task4 = new Task4();
        task4.externalSortingOfSCVFile("data.csv");
    }
}
