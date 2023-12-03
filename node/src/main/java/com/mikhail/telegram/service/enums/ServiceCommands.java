package com.mikhail.telegram.service.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    private final String value;

    ServiceCommands(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Возвращает только валидные команды из перечня enum
     *
     * @param v строка от пользователя с командой
     * @return
     */
    public static ServiceCommands fromValue(String v) {
        for (ServiceCommands c: ServiceCommands.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }

        return null;
    }
}
