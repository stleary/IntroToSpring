package com.example;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
    public String getTime() {
        LocalDate specificDate = LocalDate.of(2026, Month.JANUARY, 1);
        return " " + specificDate.toString();
    }
}