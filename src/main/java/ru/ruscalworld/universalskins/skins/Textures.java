package ru.ruscalworld.universalskins.skins;

// Текстуры. Просто текстуры скина.
public class Textures {
    // Само изображение в base64 (но это неточно)
    private final String value;
    // Подпись, которая просто нужна
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
