package ru.melnikov.neoflex.models;



import javax.persistence.*;


@Entity
@Table(name = "month_info")
public class MonthModel {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "month_number")
    private int NumberOfMonth;

    @Column(name = "holidays")
    private String holidays;

    @Column(name = "quantity_days")
    private int quantityDays;

    public int getNumberOfMonth() {
        return NumberOfMonth;
    }

    public void setNumberOfMonth(int number) {
        this.NumberOfMonth = number;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String weekends) {
        this.holidays = weekends;
    }

    public int getQuantityDays() {
        return quantityDays;
    }

    public void setQuantityDays(int quantityDays) {
        this.quantityDays = quantityDays;
    }

    @Override
    public String toString() {
        return "MonthModel{" +
                "id=" + id +
                ", NumberOfMonth=" + NumberOfMonth +
                ", weekends='" + holidays + '\'' +
                ", quantityDays=" + quantityDays +
                '}';
    }
}
