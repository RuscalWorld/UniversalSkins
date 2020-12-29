package ru.ruscalworld.universalskins.skins;

public class Textures {
    private final String value;
    private final String signature;

    public Textures(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public String getValue() {
        return value;
    }
}
