package com.mikhail.telegram.controller;

public class TestUpdates {
    public static final String MESSAGE_WITH_TEXT = "{\n" +
            "\"update_id\":10000,\n" +
            "\"message\":{\n" +
            "  \"date\":1441645532,\n" +
            "  \"chat\":{\n" +
            "     \"last_name\":\"Test Lastname\",\n" +
            "     \"id\":1111111,\n" +
            "     \"type\": \"private\",\n" +
            "     \"first_name\":\"Test Firstname\",\n" +
            "     \"username\":\"Testusername\"\n" +
            "  },\n" +
            "  \"message_id\":1365,\n" +
            "  \"from\":{\n" +
            "     \"last_name\":\"Test Lastname\",\n" +
            "     \"id\":1111111,\n" +
            "     \"first_name\":\"Test Firstname\",\n" +
            "     \"username\":\"Testusername\"\n" +
            "  },\n" +
            "  \"text\":\"/start\"\n" +
            "}\n" +
            "}";
}