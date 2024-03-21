package ru.melnikov.neoflex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.melnikov.neoflex.models.MonthModel;

@Repository
public interface VacationPayCalculationRepository extends JpaRepository<MonthModel, Integer> {


    @Query (value = "select * from month_info where month_number = ?", nativeQuery = true)
    MonthModel getInfoAboutMonthLeaving(@Param("month_number") int number);
}
