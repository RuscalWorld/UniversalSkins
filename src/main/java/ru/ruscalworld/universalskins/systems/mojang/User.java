package ru.ruscalworld.universalskins.systems.mojang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    public String id;
    public String name;

    public User() { }
}
