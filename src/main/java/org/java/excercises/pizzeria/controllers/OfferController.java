package org.java.excercises.pizzeria.controllers;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.models.Offer;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.OfferRepository;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    OfferRepository offerRepository;
    @Autowired
    PizzaRepository pizzaRepository;

    @GetMapping("/create")
    public String create(@RequestParam("pizzaId") Integer pizzaId, Model model) {
        Optional<Pizza> foundPizza = pizzaRepository.findById(pizzaId);
        if(foundPizza.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Offer offer = new Offer();
        offer.setStartDate(LocalDate.now());
        offer.setPizza(foundPizza.get());
        model.addAttribute("offer", offer);
        return "offer/form";

    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("offer") Offer formOffer,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/offer/form";
        offerRepository.save(formOffer);
        // redirect
        return "redirect:/pizza/" + formOffer.getPizza().getName();
    }


}
