package com.insurance.policy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "CUSTOMERS", indexes = {
    @Index(name = "idx_customer_email", columnList = "email", unique = true)
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank @Email @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    // Builder-like constructor for service/mapper usage
    public Customer(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

}
