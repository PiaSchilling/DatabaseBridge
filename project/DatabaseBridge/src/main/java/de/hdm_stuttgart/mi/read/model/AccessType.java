package de.hdm_stuttgart.mi.read.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Different types of table access rights/privileges
 */
public enum AccessType {
    SELECT, INSERT, UPDATE, DELETE, REFERENCES, TRIGGER, TRUNCATE, UNKNOWN;

    public static AccessType fromString(String privilegeString) {
        final Optional<AccessType> match = Arrays.stream(values()).filter(p -> p.name().equals(privilegeString)).findFirst();
        return match.orElse(UNKNOWN);
    }
}
