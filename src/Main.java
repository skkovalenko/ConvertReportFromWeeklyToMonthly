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

    private static final int INDEX_DATE = 0;
    private static final int INDEX_SUM = 1;
    private static final String PATH_SALES = "data/sales.csv";
    private static final DateTimeFormatter FORMAT_DATE_FROM_SALES = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public static void main(String[] args) throws IOException {
        ArrayList<WeeklyReport> weeklyReportsList = parseCSV(PATH_SALES);

        String stringIn = new Scanner(System.in).nextLine().trim();
        if(!stringIn.isEmpty()){
            Month month = null;
            while (month == null){
                try {
                    month = Month.valueOf(stringIn.toUpperCase());
                }catch (IllegalArgumentException e){
                    System.out.println("Неверно введено значение\nПопробуйте еще раз:");
                    stringIn = new Scanner(System.in).nextLine().trim();
                }
            }
            System.out.printf("Недельные отчеты по %s:\n", month);
            Conversion.monthlyReport(weeklyReportsList, month).forEach(System.out::println);
        }else {
            System.out.println("Месячные отчеты");
            Conversion.convertMonthliesReports(weeklyReportsList).forEach(System.out::println);
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
