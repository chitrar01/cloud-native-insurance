package com.insurance.policy.web.dto;

public record CustomerDto (
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone
){
    
}