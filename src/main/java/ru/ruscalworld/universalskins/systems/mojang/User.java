package ru.ruscalworld.universalskins.systems.mojang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Пользователь для UsersResponse
// Да, может быть, стоило использовать встроенные классы
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    // Публичные поля. Просто не знаю почему, но это плохо, не делайте так (!!!)
    public String id;
    public String name;

    // Пустой конструктор, просто чтобы Jackson работал
    public User() { }
}
