package ru.ruscalworld.universalskins.skins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {
    public String name;
    public String value;
    public String signature;

    public Property() { }
}
