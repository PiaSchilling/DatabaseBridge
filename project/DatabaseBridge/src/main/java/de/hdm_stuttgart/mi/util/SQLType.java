package de.hdm_stuttgart.mi.util;

import java.sql.Types;

public enum SQLType {
    ARRAY(Types.ARRAY, "ARRAY", false),
    BIGINT(Types.BIGINT, "BIGINT", false),
    BINARY(Types.BINARY, "BINARY", true),
    BIT(Types.BIT, "BIT", false),
    BLOB(Types.BLOB, "BLOB", true),
    BOOLEAN(Types.BOOLEAN, "BOOLEAN", false),
    CHAR(Types.CHAR, "CHAR", true),
    CLOB(Types.CLOB, "CLOB", true),
    DATALINK(Types.DATALINK, "DATALINK", false),
    DATE(Types.DATE, "DATE", false),
    DECIMAL(Types.DECIMAL, "DECIMAL", false),
    DOUBLE(Types.DOUBLE, "DOUBLE", false),
    FLOAT(Types.FLOAT, "FLOAT", false),
    INTEGER(Types.INTEGER, "INTEGER", false),
    JAVA_OBJECT(Types.JAVA_OBJECT, "JAVA_OBJECT", false),
    LONGNVARCHAR(Types.LONGNVARCHAR, "LONGNVARCHAR", false),
    LONGVARBINARY(Types.LONGVARBINARY, "LONGVARBINARY", true),
    LONGVARCHAR(Types.LONGVARCHAR, "LONGVARCHAR", true),
    NCHAR(Types.NCHAR, "NCHAR", true),
    NCLOB(Types.NCLOB, "NCLOB", true),
    NULL(Types.NULL, "NULL", false),
    NUMERIC(Types.NUMERIC, "NUMERIC", false),
    NVARCHAR(Types.NVARCHAR, "NVARCHAR", true),
    OTHER(Types.OTHER, "OTHER", false),
    REAL(Types.REAL, "REAL", false),
    REF(Types.REF, "REF", false),
    ROWID(Types.ROWID, "ROWID", false),
    SMALLINT(Types.SMALLINT, "SMALLINT", false),
    SQLXML(Types.SQLXML, "SQLXML", false),
    STRUCT(Types.STRUCT, "STRUCT", false),
    TIME(Types.TIME, "TIME", false),
    TIMESTAMP(Types.TIMESTAMP, "TIMESTAMP", false),
    TINYINT(Types.TINYINT, "TINYINT", false),
    VARBINARY(Types.VARBINARY, "VARBINARY", true),
    VARCHAR(Types.VARCHAR, "VARCHAR", true),
    UNKNOWN(-13,"UNKNOWN",false);

    private final int typeCode;
    private final String typeName;
    public final boolean hasLength;

    SQLType(int typeCode, String typeName, boolean hasLength) {
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.hasLength = hasLength;
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
