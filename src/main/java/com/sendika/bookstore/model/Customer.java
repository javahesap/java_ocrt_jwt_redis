package com.sendika.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String city;
    @NotBlank private String countryRegion;
    @Email @NotBlank private String email;
    @NotBlank private String name;
    @NotBlank private String phoneNumber;
    @NotBlank private String postalCode;
    @NotBlank private String streetAndHouseNumber;
    @NotBlank private String surname;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getCity(){return city;}
    public void setCity(String city){this.city=city;}
    public String getCountryRegion(){return countryRegion;}
    public void setCountryRegion(String countryRegion){this.countryRegion=countryRegion;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public String getPostalCode(){return postalCode;}
    public void setPostalCode(String postalCode){this.postalCode=postalCode;}
    public String getStreetAndHouseNumber(){return streetAndHouseNumber;}
    public void setStreetAndHouseNumber(String s){this.streetAndHouseNumber=s;}
    public String getSurname(){return surname;}
    public void setSurname(String surname){this.surname=surname;}
}
