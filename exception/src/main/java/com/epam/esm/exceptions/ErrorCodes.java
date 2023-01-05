package com.epam.esm.exceptions;

public final class ErrorCodes {
    public static final String SUFFIX_RESPONSE_ENTITY_EXCEPTIONS = "99";
    public static final String NOT_CHANGE_ROW = "42201";
    public static final String NOT_CREATE_ROW = "42202";
    public static final String NOT_MODIFIED_RESOURCE = "30401";
    public static final String NOT_FOUND_TAG_RESOURCE = "40401";
    public static final String NOT_FOUND_CERTIFICATE_RESOURCE = "40402";
    public static final String NO_HANDLER_FOUND = "40403";
    public static final String SQL_DUPLICATE_ENTRY = "40001";
    public static final String SQL_NULL_ENTRY = "40002";
    public static final String INVALID_CERTIFICATE_PROPERTY = "40003";
    public static final String INVALID_TAG_PROPERTY = "40004";
    public static final String INVALID_CERTIFICATE_ID_PROPERTY = "40005";
    public static final String INVALID_TAG_ID_PROPERTY = "40006";
    public static final String INVALID_TAG_NAME_PROPERTY = "40007";
    public static final String NULL_RESOURCE = "40008";
    public static final String SQL_AMBIGUOUS_COLUMN = "40009";
    public static final String INTERNAL_SERVER_ERROR = "50001";
    public static final String INTERNAL_SERVER_ILLEGAL_STATE = "50002";

    private ErrorCodes() {
    }
}
