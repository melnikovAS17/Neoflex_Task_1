package ru.melnikov.neoflex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import ru.melnikov.neoflex.filters.SimpleFilter;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);


    }

    @Bean
    public FilterRegistrationBean<SimpleFilter> filterRegistrationBean() {
        FilterRegistrationBean<SimpleFilter> registrationBean = new FilterRegistrationBean<>();
        SimpleFilter simpleFilter = new SimpleFilter();

        registrationBean.setFilter(simpleFilter); // Задаем фильтр
        registrationBean.addUrlPatterns("/start/*"); // Устанавливаем URL-шаблоны
        registrationBean.setOrder(1); // Определяем приоритет

        return registrationBean;
    }
}