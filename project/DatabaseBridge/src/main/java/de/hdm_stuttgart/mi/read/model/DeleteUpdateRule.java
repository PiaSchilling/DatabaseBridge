package de.hdm_stuttgart.mi.read.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * What happens to a foreign key when the primary key is deleted or updated
 */
public enum DeleteUpdateRule {
    /**
     * Do not allow delete or update of primary key if it has been imported
     */
    IMPORTED_NO_ACTION(3),

    /**
     * on update: change imported key to agree with primary key update
     * on delete: delete rows that import a deleted key
     */
    IMPORTED_KEY_CASCADE(0),

    /**
     * change imported key to NULL if its primary key has been deleted/updated
     */
    IMPORTED_KEY_SET_NULL(2),

    /**
     * change imported key to default if its primary key has been deleted/updated
     */
    IMPORTED_KEY_SET_DEFAULT(4),

    /**
     * same as importedKeyNoAction (for ODBC 2.x compatibility)
     */
    IMPORTED_KEY_RESTRICT(1),

    /**
     * Update rule is unknown
     */
    UNKNOWN(-13);

    final int ruleCode;

    DeleteUpdateRule(int ruleCode) {
        this.ruleCode = ruleCode;
    }

    /**
     * Get the delete or update rule from the code specified by jdbc
     * @param ruleCode a constant value defined
     *                <a href="https://docs.oracle.com/javase/8/docs/api/constant-values.html#java.sql.DatabaseMetaData.importedKeyRestrict">here</a>
     *                 which represents a delete or update rule
     * @return the DeleteUpdateRule which matches the ruleCode or UNKNOWN if the ruleCode does not match any
     */
    public static DeleteUpdateRule fromCode(short ruleCode) {
        final Optional<DeleteUpdateRule> match = Arrays.stream(values()).filter(e -> e.ruleCode == ruleCode).findFirst();
        return match.orElse(UNKNOWN);
    }

}
