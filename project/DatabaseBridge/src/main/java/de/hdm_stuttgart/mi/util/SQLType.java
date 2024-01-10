package de.hdm_stuttgart.mi.util;

import java.sql.Types;

public enum SQLType {
    ARRAY(Types.ARRAY, "ARRAY"),
    BIGINT(Types.BIGINT, "BIGINT"),
    BINARY(Types.BINARY, "BINARY"),
    BIT(Types.BIT, "BIT"),
    BLOB(Types.BLOB, "BLOB"),
    BOOLEAN(Types.BOOLEAN, "BOOLEAN"),
    CHAR(Types.CHAR, "CHAR"),
    CLOB(Types.CLOB, "CLOB"),
    DATALINK(Types.DATALINK, "DATALINK"),
    DATE(Types.DATE, "DATE"),
    DECIMAL(Types.DECIMAL, "DECIMAL"),
    DOUBLE(Types.DOUBLE, "DOUBLE"),
    FLOAT(Types.FLOAT, "FLOAT"),
    INTEGER(Types.INTEGER, "INTEGER"),
    JAVA_OBJECT(Types.JAVA_OBJECT, "JAVA_OBJECT"),
    LONGNVARCHAR(Types.LONGNVARCHAR, "LONGNVARCHAR"),
    LONGVARBINARY(Types.LONGVARBINARY, "LONGVARBINARY"),
    LONGVARCHAR(Types.LONGVARCHAR, "LONGVARCHAR"),
    NCHAR(Types.NCHAR, "NCHAR"),
    NCLOB(Types.NCLOB, "NCLOB"),
    NULL(Types.NULL, "NULL"),
    NUMERIC(Types.NUMERIC, "NUMERIC"),
    NVARCHAR(Types.NVARCHAR, "NVARCHAR"),
    OTHER(Types.OTHER, "OTHER"),
    REAL(Types.REAL, "REAL"),
    REF(Types.REF, "REF"),
    ROWID(Types.ROWID, "ROWID"),
    SMALLINT(Types.SMALLINT, "SMALLINT"),
    SQLXML(Types.SQLXML, "SQLXML"),
    STRUCT(Types.STRUCT, "STRUCT"),
    TIME(Types.TIME, "TIME"),
    TIMESTAMP(Types.TIMESTAMP, "TIMESTAMP"),
    TINYINT(Types.TINYINT, "TINYINT"),
    VARBINARY(Types.VARBINARY, "VARBINARY"),
    VARCHAR(Types.VARCHAR, "VARCHAR"),
    UNKNOWN(-13,"UNKNOWN");

    private final int typeCode;
    private final String typeName;

    SQLType(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public static SQLType fromTypeCode(int typeCode) {
        for (SQLType sqlType : values()) {
            if (sqlType.typeCode == typeCode) {
                return sqlType;
            }
        }
        return UNKNOWN;
    }
}
