package data;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class MonthlyReport {
    LocalDate date;
    long sum;
    Month month;

    public MonthlyReport(LocalDate date, long sum) {
        this.date = date;
        this.sum = sum;
        month = date.getMonth();
    }

    @Override
    public String toString() {
        return
                "date: " + date.format(DateTimeFormatter.ofPattern("MM.yyyy")) + ", sum: " + sum;
    }
}
