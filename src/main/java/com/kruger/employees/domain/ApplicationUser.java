package com.kruger.employees.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 10)
    @Column(name = "identification", length = 10, nullable = false, unique = true)
    private String identification;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "address")
    private String address;

    @Size(min = 10, max = 10)
    @Column(name = "cellphone", length = 10)
    private String cellphone;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(mappedBy = "applicationUser")
    @JsonIgnoreProperties(value = { "applicationUser" }, allowSetters = true)
    private Set<Vaccine> vaccines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentification() {
        return this.identification;
    }

    public ApplicationUser identification(String identification) {
        this.setIdentification(identification);
        return this;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public ApplicationUser birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return this.address;
    }

    public ApplicationUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellphone() {
        return this.cellphone;
    }

    public ApplicationUser cellphone(String cellphone) {
        this.setCellphone(cellphone);
        return this;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<Vaccine> getVaccines() {
        return this.vaccines;
    }

    public void setVaccines(Set<Vaccine> vaccines) {
        if (this.vaccines != null) {
            this.vaccines.forEach(i -> i.setApplicationUser(null));
        }
        if (vaccines != null) {
            vaccines.forEach(i -> i.setApplicationUser(this));
        }
        this.vaccines = vaccines;
    }

    public ApplicationUser vaccines(Set<Vaccine> vaccines) {
        this.setVaccines(vaccines);
        return this;
    }

    public ApplicationUser addVaccine(Vaccine vaccine) {
        this.vaccines.add(vaccine);
        vaccine.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeVaccine(Vaccine vaccine) {
        this.vaccines.remove(vaccine);
        vaccine.setApplicationUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", identification='" + getIdentification() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", address='" + getAddress() + "'" +
            ", cellphone='" + getCellphone() + "'" +
            "}";
    }
}
