package com.hungerbyte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street address is required")
    @Column(nullable = false)
    private String street;

    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Pincode is required")
    @Column(nullable = false, length = 20)
    private String pincode;

    @Column(length = 20)
    private String addressType = "HOME"; // HOME, WORK, OTHER

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Address() {}

    public Address(String street, String city, String state, String pincode, String addressType, Boolean isDefault, User user) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.addressType = addressType;
        this.isDefault = isDefault;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
