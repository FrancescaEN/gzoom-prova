package it.memelabs.gn.services;

public enum OfbizErrors {

    /* This ERROR MUST BE ALWAYS remain at first position because it is the default for the GnServiceException */
    GENERIC,

    ATTACHMENT_NOT_FOUND,
    AUTHORIZATION_NOT_FOUND,
    AUTHORIZATION_OUTDATED,
    AUTHORIZATION_ITEM_NOT_FOUND,
    AUTHORIZATION_TERM_NOT_FOUND,
    AUTHORIZATION_NOT_CONFLICTING,
    /**
     * Authorization with TYPE=ALBO, contains a detail that not contains almost one registration number
     */
    AUTH_DETAIL_ALBO_MISSING_VEHICLE_REG_NUM,
    /**
     * Authorization with TYPE=AIA-AUA-OTHER, contains a detail that not contains almost one operation and almost one waste
     */
    AUTH_DETAIL_AIA_AUA_OTHER_MISSING_OPERATION_OR_WASTE,
    CANNOT_LOGIN_FROM_WEB,
    COMPANY_ALREADY_EXISTS,
    DUPLICATED_AUTHORIZATION,
    DUPLICATED_ALBO_AUTHORIZATION,
    DUPLICATED_AIA_AUTHORIZATION,
    DUPLICATED_AUA_AUTHORIZATION,
    DUPLICATED_UPLOAD,
    EMAIL_NOT_VALID,
    EXPIRED_OPERATION,
    HOLDER_BASE_NOT_FOUND,
    INVALID_AUTHORIZATION_STATUS,
    INVITATION_NOT_FOUND,
    INVITATION_CANCELLED,
    INVITATION_ALREADY_ACCEPTED,
    INVITATION_CREATE_CONTACT_ERROR,
    ISSUER_NOT_FOUND,
    MISSING_DATA,
    MISSING_AUTHORIZATION_DOCUMENT,
    NEW_PASSWORD_EQUAL_OLD,
    NOT_AUTHENTICATED,
    NOT_ENOUGH_PERMISSIONS,
    OLD_PASSWORD_WRONG,
    PASSWORD_EQUAL_USERNAME,
    PASSWORD_NOT_MATCH_VERIFY,
    TAX_NUMBER_SYNTACTICALLY_INCORRECT,
    USER_NOT_LOGGED,
    USERNAME_NOT_EXISTS,
    USERNAME_NOT_UNIQUE,
    VAT_NUMBER_SYNTACTICALLY_INCORRECT,
    EXPIRED_DEVICE,
    CANNOT_DISABLE_VALIDATION_FLAG,
    PASSWORD_TOO_WEAK,

    /**
     * Error when trying to run a service without permissions (policy violation).
     */
    ACCESS_DENIED,

    /**
     * Error when running a service violating business rules or regular workflows.
     */
    BUSINESS_LOGIC_EXCEPTION,

    /**
     * All generic errors due to GenericEntityException (included constraint violation).
     */
    ENTITY_EXCEPTION,

    /**
     * Method invocation with missing or wrong required parameters.
     */
    INVALID_PARAMETERS,

    /**
     * All generic errors due to GenericServiceException.
     */
    SERVICE_EXCEPTION,

}
