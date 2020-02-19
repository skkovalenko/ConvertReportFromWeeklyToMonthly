package service;

import data.MonthlyReport;
import data.WeeklyReport;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conversion {

    private static final int WEEKDAYS = 5; // Рабочие дни недели
    private static final int ALL_DAYS_WEEK = 7; // Кол-во дней в неделю

    /**
     * метод convertMonthliesReports() преобразует недельные отчеты в месячные отчеты
     * метод monthlyReport() возвращает лист с недельными отчетами по заданному месяцу
     *
     * @param weeklyReportsList лист с недельными отчетами.
     * @param month месяц по которому нужен отчет
     * @return лист с недельными отчетами по заданному месяцу
     */

    public static List<WeeklyReport> monthlyReport(ArrayList<WeeklyReport> weeklyReportsList, Month month){
        return weeklyReportsList.stream()
                .filter(weeklyReport -> weeklyReport.getDate().getMonth() == month)
                .collect(Collectors.toList());
    }

    /**
     * метод convertMonthliesReports преобразует недельные отчеты в месячные отчеты
     * @param weeklyReportsList лист с недельными отчетами.
     * @return лист с месячными отчетами
     */
    public static List<MonthlyReport> convertMonthliesReports(ArrayList<WeeklyReport> weeklyReportsList){
        ArrayList<MonthlyReport> monthliesReportsList = new ArrayList<>();
        LocalDate previousDateWeeklyReport = weeklyReportsList.get(0).getDate();
        LocalDate dateForMonthlyReport = weeklyReportsList.get(0).getDate();
        long sumForMonthlyReport = 0;
        for (WeeklyReport weeklyReport : weeklyReportsList){
            //проверка месяца в недельном отчете с предыдущим недельным очтетом; Проверка недельного очтета на переходную неделю
            if (weeklyReport.getDate().getMonth() != previousDateWeeklyReport.getMonth()
                    && weeklyReport.getDate().minusWeeks(1).getMonth() == previousDateWeeklyReport.getMonth()) {
                //Узнать сколько будних дней в недельном очтете принадлежат новому месяцу
                int countDaysNewMonth = 0;
                for(int i = 0; i < ALL_DAYS_WEEK; i++){
                    LocalDate minusDays = weeklyReport.getDate().minusDays(i);
                    if((weeklyReport.getDate().getMonth() == minusDays.getMonth())
                            && (minusDays.getDayOfWeek() != DayOfWeek.SATURDAY
                            && minusDays.getDayOfWeek() != DayOfWeek.SUNDAY)){
                        countDaysNewMonth++;
                    }
                }
                // Добавить к общей сумме месячного отчета сумму с переходной недели и создать месячный отчет и добавить в лист с месячными отчетами
                sumForMonthlyReport += weeklyReport.getSum() / WEEKDAYS * (WEEKDAYS - countDaysNewMonth);
                monthliesReportsList.add(new MonthlyReport(dateForMonthlyReport, sumForMonthlyReport));
                //Переопределить сумму для нового месячного отчета, положив сумму с переходной недели
                sumForMonthlyReport = weeklyReport.getSum() - (weeklyReport.getSum() / WEEKDAYS * (WEEKDAYS - countDaysNewMonth));
                //Переопределить дату месячного отчета
                dateForMonthlyReport = weeklyReport.getDate();
            } else {
                sumForMonthlyReport += weeklyReport.getSum();
            }
            //Если недельный отчет поседний в списке сморировать месячный очтет
            if(weeklyReportsList.get(weeklyReportsList.size() - 1) == weeklyReport){
                monthliesReportsList.add(new MonthlyReport(dateForMonthlyReport, sumForMonthlyReport));
            }
            //переопределить дату предыдущего отчета
            previousDateWeeklyReport = weeklyReport.getDate();
        }
        return monthliesReportsList;
    }
}
