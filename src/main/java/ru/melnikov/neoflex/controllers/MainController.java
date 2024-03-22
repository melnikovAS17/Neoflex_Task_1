package ru.melnikov.neoflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.melnikov.neoflex.services.VacationPayCalculationService;

@RestController
public class MainController {

    private final VacationPayCalculationService vacationPayCalculationService;

    @Autowired
    public MainController(VacationPayCalculationService vacationPayCalculationService) {
        this.vacationPayCalculationService = vacationPayCalculationService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<String> getVacationPayAmount(@RequestParam(name = "salary") int salary,
                                               @RequestParam(name = "vacationDays") int vacationDays){

        int vacationPayAmount = vacationPayCalculationService.getVacationPayAmount(salary,vacationDays);

        return ResponseEntity.ok("Vacation pay: " + vacationPayAmount);
    }

    @GetMapping("/accurateCalculate")
    public ResponseEntity<String> getVacationPayAmountIncludingHolidays(@RequestParam(name = "salary") int salary,
                                                        @RequestParam(name = "vacationDays") int vacationDays,
                                                        @RequestParam(name = "dayOfLeaving" ) int dayOfLeaving,
                                                        @RequestParam(name = "monthOfLeaving") int monthOfLeaving) {
        int vacationPayAmount = vacationPayCalculationService
                .getVacationPayAmountIncludingHolidays(salary,vacationDays, dayOfLeaving, monthOfLeaving);

        return ResponseEntity.ok("Vacation pay: " + vacationPayAmount);
    }
}
