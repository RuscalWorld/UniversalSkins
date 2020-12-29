package ru.ruscalworld.universalskins.systems.mojang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.ruscalworld.universalskins.skins.Property;

import java.util.List;

// Ответ от Mojang API, возвращаемый при получении текстур по UUID
@JsonIgnoreProperties(ignoreUnknown = true)
public class MojangResponse {
    // Публичные поля. Просто не знаю почему, но это плохо, не делайте так (!!!)
    public String id;
    public String name;
    public List<Property> properties;

    // Пустой конструктор, просто чтобы Jackson работал
    public MojangResponse() { }
}
