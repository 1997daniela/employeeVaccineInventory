package com.kruger.employees.repository;

import com.kruger.employees.domain.Vaccine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vaccine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {}
