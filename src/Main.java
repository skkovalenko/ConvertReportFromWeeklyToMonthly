import data.WeeklyReport;
import service.Conversion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String MESSAGE_START = "Введите параметр \"month\", чтобы получить данные о месячных продажах." +
            "\nВведите один из месяцев например \"february\", чтобы получить недельные данные о продажах за месяц." +
            "\nЕсли параметр не указан, выводятся данные по всем месяцам:";
    private static final String MESSAGE_WRONG_VALUE = "Неверно введено значение\nПопробуйте еще раз:";
    private static final String MESSAGE_WEEKLY_ORDER = "Недельные отчеты за ";
    private static final String MESSAGE_ALL_ORDER = "Данные по всем месяцам:";
    private static final String MESSAGE_MONTHLIES_ORDER = "Данные о месячных продажах:";
    private static final String PARAMETER = "MONTH";
    private static final int INDEX_DATE = 0;
    private static final int INDEX_SUM = 1;
    private static final String PATH_SALES = "data/sales.csv";
    private static final DateTimeFormatter FORMAT_DATE_FROM_SALES = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public static void main(String[] args) throws IOException {
        ArrayList<WeeklyReport> weeklyReportsList = parseCSV(PATH_SALES);
        //Пытаемся получить параметр month или
        System.out.println(MESSAGE_START);
        String stringIn = new Scanner(System.in).nextLine().trim().toUpperCase();
        //Проверка на да
        if(!stringIn.isEmpty()){
            if(stringIn.equals(PARAMETER)){
                System.out.println(MESSAGE_MONTHLIES_ORDER);
                Conversion.convertMonthliesReports(weeklyReportsList).forEach(System.out::println);
                return;
            }
            Month month = null;
            while (month == null){
                try {
                    month = Month.valueOf(stringIn.toUpperCase());
                }catch (IllegalArgumentException e){
                    System.out.println(MESSAGE_WRONG_VALUE);
                    stringIn = new Scanner(System.in).nextLine().trim();
                }
            }
            System.out.println(MESSAGE_WEEKLY_ORDER + month);
            Conversion.monthlyReport(weeklyReportsList, month).forEach(System.out::println);
        }else {
            System.out.println(MESSAGE_ALL_ORDER);
            weeklyReportsList.forEach(System.out::println);
        }
    }
    //парсинг sales.csv и преоброзование в лист с еженедельными отчетами
    private static ArrayList<WeeklyReport> parseCSV(String pathFrom) throws IOException {
        ArrayList<WeeklyReport> weeklyReportsList = new ArrayList<>();
        Files.readAllLines(Paths.get(pathFrom)).stream()
                .filter(s -> s.matches("^\\d+\\.\\d+\\.\\d+;\\d+$"))// чтобы исключить заголовок sales.csv
                .map(s -> s.split(";"))
                .forEach(stringsArr -> weeklyReportsList.add(new WeeklyReport(
                        LocalDate.parse(stringsArr[INDEX_DATE], FORMAT_DATE_FROM_SALES),
                        Long.parseLong(stringsArr[INDEX_SUM]))));
        return weeklyReportsList;
    }
}
