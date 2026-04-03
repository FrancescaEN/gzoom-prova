package it.memelabs.gn.services.communication;


public enum CommunicationEventAttributeKeyOfbiz {
    AUTHORIZATION_KEY,
    AUTHORIZATION_DESCRIPTION,
    AUTHORIZATION_NUMBER,
    AUTHORIZATION_HOLDER,  //should contain description of holder belong to company
    AUTHORIZATION_HOLDER_BASE,  //should contain description of company base
    /**
     * Temporary UUID generated during password recovery. Necessary to generate temporary URLs.
     */
    TEMPORARY_UUID,
    USER_ENABLED,
    OWNER_NODE_DESCRIPTION,

    //jobEvent
    AUTH_EXPIRATION_RECIPIENT , //"holder" | "user" (nuovo) //due costanti
    AUTH_EXPIRATION_TYPE,//[int]: 1, 2, 3, etc.  questa costante, assieme a recipient_type permetterà di definire il contenuto del messaggio (nuovo)       // insieme rappresentano il valore HN1, UN1...... del caso d'uso
    AUTH_EXPIRATION_DATE ,// 18/07/2015 (nuovo)
    AUTH_EXPIRATION_LEFT_MONTHS,//[int]: 8, 7, 6, etc. (nuovo)
    AUTHORIZATION_TYPE_ID,//ALBO, AIA, AUA, OTHER // mantenere le attuali costanti (nuovo) da convertire i valori di AuthorizationTypesOfbiz
    ISSUER, // Ministero Cippa Lippa (nuovo)
    ISSUER_BASE,  //Sezione provinciale di Prato (nuovo)
    AUTHORIZATION_DETAILS,   // distinguere due casi: ALBO e tutti gli altri (nuovo)
    //details
    CATEGORY_CODE,          // for auth ALBO
    CATEGORY_DESCRIPTION,   // { category_code: "1", "category_description" : "raccolta e spazzamento.." },
    DETAIL_DESCRIPTION,      // for other:   { detail_description: "dettaglio 1" },

    INV_EXPIRATION_TYPE,//[int]: 1, 2, 3.  questa costante permetterà di definire il contenuto del messaggio (nuovo)       // insieme rappresentano il valore IN1, IN2, IN3...... del caso d'uso
    INVITER_NAME,//name of the inviter company
    INVITED_NAME,//name of the invited company name
    INV_EXPIRATION_DATE,//invitation expiration date
    INV_EXPIRATION_LEFT_DAYS,//[int]: 8, 7, 6, etc.
    INVITATION_UUID //uuid invitation
    ;


}

/*

- auth_expiration_recipient [String]: "holder" | "user" (nuovo) //due costanti                                                                                                                                   // queste due righe
        - auth_expiration_type [int]: 1, 2, 3, etc. // questa costante, assieme a recipient_type permetterà di definire il contenuto del messaggio (nuovo)       // insieme rappresentano il valore HN1, UN1...... del caso d'uso
        - auth_expiration_date [Date]: 18/07/2015 (nuovo)
        - auth_expiration_left_months [int]: 8, 7, 6, etc. (nuovo)
        - authorization_number [String]:  BO/010101 (esiste già!)
        - authorization_kind [String]: ALBO, AIA, AUA, OTHER // mantenere le attuali costanti (nuovo) da convertire i valori di AuthorizationTypesOfbiz
        - authorization_holder [String]: Alfarec Srl // ragione sociale  (esiste già!)
        - issuer [String]: Ministero Cippa Lippa (nuovo)
        - issuer_base [String]: Sezione provinciale di Prato (nuovo)
        - authorization_details [List<Map<String,Object>>]: // distinguere due casi: ALBO e tutti gli altri (nuovo)
        - albo:
        [
        { category_code: "1", "category_description" : "raccolta e spazzamento.." },
        { category_code: "2", "category_description" : "raccolta rifiuti..." },
        ..
        ]
        - tutti gli altri:
        [
        { detail_description: "dettaglio 1" },
        { detail_description: "dettaglio 2" },
        ..
        ]
*/
