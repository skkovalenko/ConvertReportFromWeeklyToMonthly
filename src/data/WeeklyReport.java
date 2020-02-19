package data;

import java.time.LocalDate;

public class WeeklyReport {

    LocalDate date;
    long sum;

    public WeeklyReport(LocalDate date, long sum) {
        this.date = date;
        this.sum = sum;
    }

    public LocalDate getDate() {
        return date;
    }
    public long getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "date: " + date + ", sum: " + sum;
    }
}
