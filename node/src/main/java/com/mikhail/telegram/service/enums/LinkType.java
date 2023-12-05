package com.mikhail.telegram.service.enums;

public enum LinkType {
    GET_DOC("file/download-doc"),

    GET_PHOTO("file/download-photo");

    private final String link;

    LinkType(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
