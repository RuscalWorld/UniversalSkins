package ru.ruscalworld.universalskins.systems.mojang;

import java.util.List;

// Ответ от Mojang API, возвращаемый при получении никнейма по UUID
public class UsersResponse {
    // Публичные поля. Просто не знаю почему, но это плохо, не делайте так (!!!)
    public List<User> response;

    // Пустой конструктор, просто чтобы Jackson работал
    public UsersResponse() { }
}
