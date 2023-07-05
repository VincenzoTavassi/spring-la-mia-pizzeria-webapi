package org.java.excercises.pizzeria.service;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.dto.PizzaForm;
import org.java.excercises.pizzeria.exceptions.NotUniqueNameException;
import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    PizzaRepository pizzaRepository;

    // Metodo di get per tutte le pizze che può avere o meno un search term
    public Page<Pizza> getAll(Optional<String> keyword, Integer size, Integer page) {
        Pageable pageable = PageRequest.of(page, size); // Creo un pageable con i parametri passati dal controller
        if (keyword.isPresent()) return pizzaRepository.findByNameContainingIgnoreCase(keyword.get(), pageable);
        return pizzaRepository.findAll(pageable);
    }

    // Ottieni la pizza in base all'id
    public Pizza getById(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isPresent()) return foundPizza.get();
        else throw new PizzaNotFoundException("Pizza not found");
    }

    public Pizza create(Pizza pizza) throws NotUniqueNameException {
        if(!isUniqueName(pizza)) throw new NotUniqueNameException("Pizza name must be unique");
        Pizza pizzaToSave = new Pizza();
        pizzaToSave.setCreatedAt(LocalDateTime.now());
        pizzaToSave.setName(pizza.getName());
        pizzaToSave.setDescription(pizza.getDescription());
        pizzaToSave.setPrice(pizza.getPrice());
        pizzaToSave.setIngredients(pizza.getIngredients());
        pizzaToSave.setOffers(pizza.getOffers());
        pizzaToSave.setPictureUrl(pizza.getPictureUrl());
        pizzaToSave.setDBimage(pizza.getDBimage());
        return pizzaRepository.save(pizzaToSave);
    }

    public Pizza create(PizzaForm pizzaForm) throws NotUniqueNameException {
        Pizza pizzaToSave = fromPizzaFormToPizza(pizzaForm);
        return create(pizzaToSave);
    }

    public Pizza update(Integer id, Pizza pizza) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isEmpty()) throw new PizzaNotFoundException("Pizza not found");
        Pizza pizzaToSave = new Pizza();
        pizzaToSave.setId(id);
        pizzaToSave.setName(pizza.getName());
        pizzaToSave.setDescription(pizza.getDescription());
        pizzaToSave.setPrice(pizza.getPrice());
        pizzaToSave.setIngredients(pizza.getIngredients());
        pizzaToSave.setOffers(pizza.getOffers());
        pizzaToSave.setPictureUrl(pizza.getPictureUrl());
        return pizzaRepository.save(pizzaToSave);
    }

    public Pizza update(PizzaForm pizza) throws PizzaNotFoundException {
        Pizza pizzaToSave = fromPizzaFormToPizza(pizza);
        Pizza pizzaDB = getById(pizza.getId());
        if(!pizzaDB.getName().equalsIgnoreCase(pizzaToSave.getName()) && !isUniqueName(pizzaToSave)) throw new NotUniqueNameException("Il nome della pizza deve essere unico");
        pizzaDB.setName(pizzaToSave.getName());
        pizzaDB.setPictureUrl(pizzaToSave.getPictureUrl());
        pizzaDB.setDBimage(pizzaToSave.getDBimage());
        pizzaDB.setDescription(pizzaToSave.getDescription());
        pizzaDB.setIngredients(pizzaToSave.getIngredients());
        pizzaDB.setPrice(pizzaToSave.getPrice());
        return pizzaRepository.save(pizzaDB);
    }

    public void delete(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isEmpty()) throw new PizzaNotFoundException("Pizza not found");
        pizzaRepository.delete(foundPizza.get());
    }

    // Metodi CUSTOM

    // Metodo per trasformare una Pizza in DTO PizzaForm
    public PizzaForm fromPizzaToPizzaForm(Pizza pizza) {
        PizzaForm pizzaForm = new PizzaForm();
        pizzaForm.setId(pizza.getId());
        pizzaForm.setCreatedAt(pizza.getCreatedAt());
        pizzaForm.setName(pizza.getName());
        pizzaForm.setDescription(pizza.getDescription());
        pizzaForm.setPrice(pizza.getPrice());
        pizzaForm.setIngredients(pizza.getIngredients());
        pizzaForm.setPictureUrl(pizza.getPictureUrl());
        return pizzaForm;
    }

    public Pizza fromPizzaFormToPizza(PizzaForm pizzaForm) {
        Pizza pizzaToSave = new Pizza();
        pizzaToSave.setName(pizzaForm.getName());
        pizzaToSave.setPrice(pizzaForm.getPrice());
        pizzaToSave.setDescription(pizzaForm.getDescription());
        pizzaToSave.setIngredients(pizzaForm.getIngredients());
        pizzaToSave.setPictureUrl(pizzaForm.getPictureUrl());
        // Invoco il metodo custom per conversione in bytes array
        pizzaToSave.setDBimage(multipartFileToByteArray(pizzaForm.getImageFile()));
        return pizzaToSave;
    }
    private boolean isUniqueName(Pizza pizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(pizza.getName());
        return foundPizza.isEmpty();
    }

    // Metodo per convertire un file multipart ricevuto da un form in un array di bytes
    private byte[] multipartFileToByteArray(MultipartFile file) {
        byte[] bytes = null;
        if (file != null && !file.isEmpty()) {
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
