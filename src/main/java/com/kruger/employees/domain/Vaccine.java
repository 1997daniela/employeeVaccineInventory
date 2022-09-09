package com.kruger.employees.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kruger.employees.domain.enumeration.VaccineType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Vaccine.
 */
@Entity
@Table(name = "vaccine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vaccine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vaccine_type", nullable = false)
    private VaccineType vaccineType;

    @NotNull
    @Column(name = "vaccination_date", nullable = false)
    private LocalDate vaccinationDate;

    @NotNull
    @Column(name = "doses", nullable = false)
    private Integer doses;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "internalUser", "vaccines" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vaccine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType getVaccineType() {
        return this.vaccineType;
    }

    public Vaccine vaccineType(VaccineType vaccineType) {
        this.setVaccineType(vaccineType);
        return this;
    }

    public void setVaccineType(VaccineType vaccineType) {
        this.vaccineType = vaccineType;
    }

    public LocalDate getVaccinationDate() {
        return this.vaccinationDate;
    }

    public Vaccine vaccinationDate(LocalDate vaccinationDate) {
        this.setVaccinationDate(vaccinationDate);
        return this;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public Integer getDoses() {
        return this.doses;
    }

    public Vaccine doses(Integer doses) {
        this.setDoses(doses);
        return this;
    }

    public void setDoses(Integer doses) {
        this.doses = doses;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Vaccine applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaccine)) {
            return false;
        }
        return id != null && id.equals(((Vaccine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaccine{" +
            "id=" + getId() +
            ", vaccineType='" + getVaccineType() + "'" +
            ", vaccinationDate='" + getVaccinationDate() + "'" +
            ", doses=" + getDoses() +
            "}";
    }
}
