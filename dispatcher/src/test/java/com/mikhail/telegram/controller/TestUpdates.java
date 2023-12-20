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

    public static final String MESSAGE_WITH_DOCUMENT = "{\n" +
            "\"update_id\":10000,\n" +
            "\"message\":{\n" +
            "  \"date\":1441645532,\n" +
            "  \"chat\":{\n" +
            "     \"last_name\":\"Test Lastname\",\n" +
            "     \"type\": \"private\",\n" +
            "     \"id\":1111111,\n" +
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
            "  \"document\": {\n" +
            "      \"file_id\": \"AwADBAADbXXXXXXXXXXXGBdhD2l6_XX\",\n" +
            "      \"file_name\": \"Testfile.pdf\",\n" +
            "      \"mime_type\": \"application/pdf\",\n" +
            "      \"file_size\": 536392\n" +
            "  }\n" +
            "}\n" +
            "}";
}