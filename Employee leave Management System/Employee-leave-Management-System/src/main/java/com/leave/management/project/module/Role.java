package com.leave.management.project.module;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN,
    EMPLOYEE;
    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
