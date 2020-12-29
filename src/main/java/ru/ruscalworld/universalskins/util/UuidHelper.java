package ru.ruscalworld.universalskins.util;

import java.util.UUID;

public class UuidHelper {
    /**
     * Конвертирует UUID без '-' в UUID с '-'
     * Например, 'fecb292849ee11ebb3780242ac130002' конвертируется в 'fecb2928-49ee-11eb-b378-0242ac130002'
     * Вообще, это было скопировано со StackOverflow
     * @param trimmedUUID UUID без минусов
     * @return UUID с минусами
     * @throws IllegalArgumentException если в качестве входного значения было передано что-то отличное от UUID
     */
    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }
}
