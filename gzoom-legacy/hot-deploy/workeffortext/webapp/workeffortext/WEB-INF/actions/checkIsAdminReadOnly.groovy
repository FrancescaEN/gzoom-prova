import org.ofbiz.base.util.*;

/**
 * Sfruttando il valore di canModifyEtchPermission, valorizza isAdmin in base al fatto che l'utente e' amministratore del modulo o meno
 * Nel caso di voce di menu Obiettivo e schede Obiettivo, il permesso da controllare canModifyEtchPermission e' vuoto,
 * quindi il permesso da controllare e' WORKEFFORTMGR_ADMIN
 */
def nowStamp = UtilDateTime.nowTimestamp();
 
context.canModifyEtchPermission = UtilValidate.isNotEmpty(context.canModifyEtchPermission) ? context.canModifyEtchPermission : "WORKEFFORTMGR_ADMIN";
Debug.log("Script checkIsAdminReadOnly.groovy context.canModifyEtchPermission " + context.canModifyEtchPermission);
context.isAdmin = checkModifyPermissions();

def checkModifyPermissions() {
    return security.hasPermission(context.canModifyEtchPermission, userLogin);
}
Debug.log("context.isAdmin " + context.isAdmin);

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext//webapp//workeffortext//WEB-INF//actions//checkIsAdminReadOnly.groovy");

