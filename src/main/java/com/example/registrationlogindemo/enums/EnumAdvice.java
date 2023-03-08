package com.example.registrationlogindemo.enums;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class EnumAdvice {
    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }
}