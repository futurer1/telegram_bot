package com.mikhail.telegram.controller;

public class TestUpdates {
    public static final String MESSAGE_WITH_TEXT = "{\n" +
            "\"update_id\":10000,\n" +
            "\"message\":{\n" +
            "  \"date\":1441645532,\n" +
            "  \"chat\":{\n" +
            "     \"last_name\":\"Test Lastname\",\n" +
            "     \"id\":111111111,\n" +
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
            "     \"id\":111111111,\n" +
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

    public static final String MESSAGE_WITH_PHOTO = "{\n" +
            "   \"update_id\":354299597,\n" +
            "   \"message\":{\n" +
            "      \"message_id\":254,\n" +
            "      \"from\":{\n" +
            "         \"id\":111111111,\n" +
            "         \"first_name\":\"Test\",\n" +
            "         \"is_bot\":false,\n" +
            "         \"last_name\":\"Testest\",\n" +
            "         \"username\":\"test\",\n" +
            "         \"language_code\":\"en\"\n" +
            "      },\n" +
            "      \"date\":1633612879,\n" +
            "      \"chat\":{\n" +
            "         \"id\":111111111,\n" +
            "         \"type\":\"private\",\n" +
            "         \"first_name\":\"Test\",\n" +
            "         \"last_name\":\"Testest\",\n" +
            "         \"username\":\"test\"\n" +
            "      },\n" +
            "      \"photo\":[\n" +
            "         {\n" +
            "            \"file_id\":\"AgACAgIAAxkBAAP-YV70TprIjRvbHfF4-C7pwh3D2nIAAkK3MRuaavlKEP9YVKoHzMsBAAMCAANzAAMhBA\",\n" +
            "            \"file_unique_id\":\"AQADQrcxG5pq-Up4\",\n" +
            "            \"width\":90,\n" +
            "            \"height\":67,\n" +
            "            \"file_size\":1420\n" +
            "         },\n" +
            "         {\n" +
            "            \"file_id\":\"AgACAgIAAxkBAAP-YV70TprIjRvbHfF4-C7pwh3D2nIAAkK3MRuaavlKEP9YVKoHzMsBAAMCAANtAAMhBA\",\n" +
            "            \"file_unique_id\":\"AQADQrcxG5pq-Upy\",\n" +
            "            \"width\":320,\n" +
            "            \"height\":240,\n" +
            "            \"file_size\":19617\n" +
            "         },\n" +
            "         {\n" +
            "            \"file_id\":\"AgACAgIAAxkBAAP-YV70TprIjRvbHfF4-C7pwh3D2nIAAkK3MRuaavlKEP9YVKoHzMsBAAMCAAN4AAMhBA\",\n" +
            "            \"file_unique_id\":\"AQADQrcxG5pq-Up9\",\n" +
            "            \"width\":350,\n" +
            "            \"height\":263,\n" +
            "            \"file_size\":21322\n" +
            "         }\n" +
            "      ]\n" +
            "   }\n" +
            "}";
}