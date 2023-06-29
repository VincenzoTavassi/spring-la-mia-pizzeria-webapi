package org.java.excercises.pizzeria.repository;

import org.java.excercises.pizzeria.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
}
