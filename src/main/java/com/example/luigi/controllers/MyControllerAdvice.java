package com.example.luigi.controllers;

import com.example.luigi.sessions.Identificatie;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
//@ControllerAdvice gebruik je voor een class die bij elke request extra data doorgeeft aand ThymeLeaf. Je maakt zo ook van de class een Bean.
//Je kan dus dependency injection toepassen in de class.
public class MyControllerAdvice {
    private final Identificatie identificatie; //Dit is de session data die je wilt doorgeven aan de thymeleaf pagina.

    MyControllerAdvice(Identificatie identificatie) { //Je inject de bean met deze session data
        this.identificatie = identificatie;
    }
    @ModelAttribute //Deze method gaat data doorgeven aan de thymeleaf page.
    void extraDataToevoegenAanModel(Model model){ //Je voorziet een Model parameter -> is de Model in ModelAndView
        model.addAttribute(identificatie);
    }

    //Vanaf nu roept Spring bij elke request de method extraDataToevoegenAanModel op. Elke request geeft nu session data door aan de Thymeleaf page.
}
