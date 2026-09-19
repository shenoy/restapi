package com.restapi.halliburton.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    public UserEntity() {}

    public UserEntity(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail(){ return  email; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email){ this.email = email; }
}
