package ru.melnikov.neoflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.melnikov.neoflex.models.MonthModel;
import ru.melnikov.neoflex.repositories.VacationPayCalculationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VacationPayCalculationService {

    private final VacationPayCalculationRepository vacationPayCalculationRepository;

    @Autowired
    public VacationPayCalculationService(VacationPayCalculationRepository vacationPayCalculationRepository) {
        this.vacationPayCalculationRepository = vacationPayCalculationRepository;
    }

    // 22 - взял, как среднее количество дней в месяце
    private static final int AVERAGE_DAYS_IN_MONTH = 22;
    // NDFL - подоходний налог
    private static final float NDFL = 0.13f;

    // Метод расчёта отпускных без учёта праздников
    public int getVacationPayAmount(int avgSalary, int amountVacationDays) {

        int tax = (int) (((avgSalary/AVERAGE_DAYS_IN_MONTH)*amountVacationDays) * NDFL);
        return ((avgSalary/AVERAGE_DAYS_IN_MONTH)*amountVacationDays) - tax;
    }

    // Метод расчёта с учётом праздников
    public int getVacationPayAmountIncludingHolidays(int avgSalary, int amountVacationDays, int dayOfLeaving,
                                                     int monthOfLeaving){
        int notTakenIntoAccountDays = 0;
        MonthModel currentMonth = getMonthInfo(monthOfLeaving);
        // Количество дней до конца месяца, получаем, вычитая из количества дней в месяце
        // день ухода в отпуск
        int daysUntilEndMonth = currentMonth.getQuantityDays() - dayOfLeaving;

        // Получаем список праздничных дней в текущем месяце
        List<Integer> currentMonthHolidays = getHolidaysNumberOfDay(currentMonth);
        // Проверяем, попадают ли дни отпуска на какие-то гос праздники
        for(int i = dayOfLeaving; i < currentMonth.getQuantityDays() + 1; i++){
            //если да, увелич кол-во неучавствующих дней в оплате
            if(currentMonthHolidays.contains(i)){
                notTakenIntoAccountDays++;
            }
        }

        // Если количество дней отпуска больше, чем дней до конца месяца,
        // тогда надо получить информацию о следующем месяце
        if(amountVacationDays > daysUntilEndMonth){
            // количество дней, которое осталось у сотрудника в следующем месяце
            int weekendsLeftInNextMonth = amountVacationDays - daysUntilEndMonth;

            notTakenIntoAccountDays += getNotTakenIntoAccountDaysNextMonth(
                    currentMonth,monthOfLeaving,weekendsLeftInNextMonth);

        }

        int tax = (int) (((avgSalary/AVERAGE_DAYS_IN_MONTH )* (amountVacationDays - notTakenIntoAccountDays)) * NDFL);
        return ((avgSalary/AVERAGE_DAYS_IN_MONTH)  * (amountVacationDays - notTakenIntoAccountDays)) - tax;
    }

    // Получаем номера праздничных дней, парсим их к int, т к в бд они хранятся в виде строки: "23,24,..."
    private List<Integer> getHolidaysNumberOfDay(MonthModel monthModel){
        return Arrays.stream(monthModel.getHolidays().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    // Получаем объект из бд
    private MonthModel getMonthInfo(int monthOfLeaving){
        return vacationPayCalculationRepository.getInfoAboutMonthLeaving(monthOfLeaving);
    }

    // Данный метолд вернёт кол-во дней неучаствующих в оплате отпуска, но он учитывает праздники
    // следующего месяца
    private int getNotTakenIntoAccountDaysNextMonth(MonthModel currentMonth, int monthOfLeaving,
                                                    int weekendsLeftInNextMonth) {
        int notTakenIntoAccountDays = 0;
        //Номер след месяца - увеличиваем число месяца ухода на один,
        // а затем получаем информацию об этом месяце
        int nextMonthNumber = ++monthOfLeaving;

        //Проверка, если сотрудник ушёл в конце декабря, следующий номер месяца полученный из бд должен
        // быть не 13 (такого не сущ), а 1
        if(currentMonth.getNumberOfMonth() == 12){
            nextMonthNumber = 1;
        }

        MonthModel nextMonth = getMonthInfo(nextMonthNumber);

        // получаем список праздничных дней следующего месяца
        List<Integer> nextMonthHolidays = getHolidaysNumberOfDay(nextMonth);

        //Проводим проверку попадает ли какой-то день отпуска на гос праздник
        for(int a = 1; a < weekendsLeftInNextMonth + 1; a++ ){
            if(nextMonthHolidays.contains(a)){
                //если да, увелич кол-во неучавств дней в оплате
                notTakenIntoAccountDays++;
            }
        }

        return notTakenIntoAccountDays;
    }

}
