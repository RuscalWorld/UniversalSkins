package ru.ruscalworld.universalskins.systems.mojang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.ruscalworld.universalskins.skins.Property;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MojangResponse {
    public String id;
    public String name;
    public List<Property> properties;

    public MojangResponse() { }
}
