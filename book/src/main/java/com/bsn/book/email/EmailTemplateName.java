package com.bsn.book.email;

public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account");
    @SuppressWarnings("unused")
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
