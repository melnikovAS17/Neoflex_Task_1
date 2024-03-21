package ru.melnikov.neoflex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.melnikov.neoflex.services.VacationPayCalculationService;

@Controller
public class MainController {

    private final VacationPayCalculationService vacationPayCalculationService;

    @Autowired
    public MainController(VacationPayCalculationService vacationPayCalculationService) {
        this.vacationPayCalculationService = vacationPayCalculationService;
    }

    @GetMapping("/calculacte/{salary}/{vacationDays}")
    public String getVacationPayAmount(Model model, @PathVariable(name = "salary") int salary,
                                       @PathVariable(name = "vacationDays") int vacationDays) {

        int vacationPayAmount = vacationPayCalculationService.getVacationPayAmount(salary,vacationDays);
        model.addAttribute("amountOfMoney", vacationPayAmount);
        return "view";
    }

    // Решил добавить в запрос параметры для уточнения вводимой информации
    @GetMapping("/accurateCalculacte/{salary}/{vacationDays}")
    public String getVacationPayAmountIncludingHolidays(Model model, @PathVariable(name = "salary") int salary,
                                                        @PathVariable(name = "vacationDays") int vacationDays,
                                                        @RequestParam(name = "dayOfLeaving" ) int dayOfLeaving,
                                                        @RequestParam(name = "monthOfLeaving") int monthOfLeaving) {
        int vacationPayAmount = vacationPayCalculationService
                .getVacationPayAmountIncludingHolidays(salary,vacationDays, dayOfLeaving, monthOfLeaving);
        model.addAttribute("amountOfMoneyExactCalc", vacationPayAmount);
        return "view";
    }

}
