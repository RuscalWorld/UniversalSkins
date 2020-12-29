package ru.ruscalworld.universalskins.skins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Свойство. Можно встретить при получении текстур из API Mojang и ely.by
@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {
    // Публичные поля. Просто не знаю почему, но это плохо, не делайте так (!!!)
    public String name;
    public String value;
    public String signature;

    public Property() { }
}
