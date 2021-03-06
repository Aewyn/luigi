package com.example.luigi.controllers;

import com.example.luigi.domain.Pizza;
import com.example.luigi.exceptions.KoersClientException;
import com.example.luigi.forms.VanTotPrijsForm;
import com.example.luigi.services.EuroService;
import com.example.luigi.services.PizzaService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.nio.charset.MalformedInputException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("pizzas")
public class PizzaController {

    private final EuroService euroService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PizzaService pizzaService;
    private final Pizza[] allePizzas = {
            new Pizza(1, "Prosciutto", BigDecimal.valueOf(4), true),
            new Pizza(2, "Margherita", BigDecimal.valueOf(5), false),
            new Pizza(3, "Calzone", BigDecimal.valueOf(4), false)
    };

    public PizzaController(EuroService euroService, PizzaService pizzaService) {
        this.euroService = euroService;
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ModelAndView findAll() {
        return new ModelAndView("pizzas", "allePizzas", allePizzas);
    }

    @GetMapping("{id}")
    public ModelAndView findById(@PathVariable long id) {
        var modelAndView = new ModelAndView("pizza");
        pizzaService.findById(id)
                .ifPresent(gevondenPizza -> {
                    modelAndView.addObject(gevondenPizza);
                    try {
                        modelAndView.addObject(
                                "inDollar", euroService.naarDollar(gevondenPizza.getPrijs()));
                    } catch (KoersClientException ex) {
                        logger.error("Kan dollar koers niet lezen", ex);
                    }
                });
        return modelAndView;
    }

    private Stream<BigDecimal> findPrijzenHelper() {
        return Arrays.stream(allePizzas).map(Pizza::getPrijs).distinct().sorted();
    }

    @GetMapping("prijzen")
    public ModelAndView prijzen() {
        return new ModelAndView("pizzasperprijs", "prijzen", pizzaService.findUniekePrijzen());
    }

    @GetMapping("prijzen/{prijs}")
    public ModelAndView findByPrijs(@PathVariable BigDecimal prijs) {
        return new ModelAndView("pizzasperprijs", "pizzas", pizzaService.findByPrijs(prijs))
                .addObject("prijzen", pizzaService.findUniekePrijzen());
    }

    @GetMapping("aantalpizzasperprijs")
    public ModelAndView findAantalPizzasPerPrijs(){
        return new ModelAndView("aantalpizzasperprijs", "aantalPizzasPerPrijs", pizzaService.findAantalPizzasPerPrijs());
    }

    @GetMapping("vantotprijs")
    public ModelAndView findByPrijsBetween(@Valid VanTotPrijsForm form, Errors errors){
        var modelAndView = new ModelAndView("vantotprijs");
        if(errors.hasErrors()){
            return modelAndView;
        }
        return modelAndView.addObject("pizzas", pizzaService.findByPrijsBetween(form.van(), form.tot()));
    }
    @GetMapping("vantotprijs/form")
    public ModelAndView vanTotPrijsForm(){
        return new ModelAndView("vantotprijs").addObject(new VanTotPrijsForm(null, null));
    }

    @GetMapping("toevoegen/form")
    public ModelAndView toevoegenForm(){
        return new ModelAndView("toevoegen").addObject(new Pizza(0, "", null, false));
    }

    @PostMapping
    public String toegoeven(@Valid Pizza pizza, Errors errors, RedirectAttributes redirect){
        if(errors.hasErrors()){
            return "toevoegen";
        }
        pizzaService.create(pizza);
        redirect.addAttribute("idNieuwePizza", pizzaService.create(pizza));
        return "redirect:/pizzas";
    }
}