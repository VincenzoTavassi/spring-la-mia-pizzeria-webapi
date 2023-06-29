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
import org.springframework.validation.FieldError;
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
        offer.setEndDate(LocalDate.now().plusDays(1));
        offer.setPizza(foundPizza.get());
        model.addAttribute("offer", offer);
        return "offer/form";

    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("offer") Offer formOffer,
                           BindingResult bindingResult) {
        if (formOffer.getEndDate().isBefore(formOffer.getStartDate())) bindingResult.addError(new FieldError("offer", "endDate", formOffer.getEndDate(), false, null, null, "La data di fine offerta deve essere superiore a quella iniziale"));
        if (bindingResult.hasErrors()) return "/offer/form";
        offerRepository.save(formOffer);
        // redirect
        return "redirect:/pizza/" + formOffer.getPizza().getName();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Offer> offer = offerRepository.findById(id);
        if (offer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // invio offer al model
        model.addAttribute("offer", offer.get());
        return "/offer/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Integer id,
            @Valid @ModelAttribute("offer") Offer offer,
            BindingResult bindingResult) {
        // verifico che esiste
        Optional<Offer> foundOffer = offerRepository.findById(id);
        if (foundOffer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (offer.getEndDate().isBefore(offer.getStartDate())) bindingResult.addError(new FieldError("offer", "endDate", offer.getEndDate(), false, null, null, "La data di fine offerta deve essere superiore a quella iniziale"));
        if (bindingResult.hasErrors()) return "/offer/form";
        // setto l'id
        offer.setId(id);
        offerRepository.save(offer);
        return "redirect:/pizza/" + offer.getPizza().getName();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Offer> foundOffer = offerRepository.findById(id);
        if (foundOffer.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        offerRepository.delete(foundOffer.get());
        return "redirect:/pizza/" + foundOffer.get().getPizza().getName();
    }


}
