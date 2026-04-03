package com.mapsengineering.base.birt.util;

public class UtilStrings {

    /**
     * Replaces all occurrences of oldString in mainString with newString
     * @param mainString The original string
     * @param oldString The string to replace
     * @param newString The string to insert in place of the old
     * @return mainString with all occurrences of oldString replaced by newString
     */
    public static String replaceString(String mainString, String oldString, String newString) {
        if (mainString == null) {
            return null;
        }
        if ((oldString == null) || (oldString.length() == 0)) {
            return mainString;
        }
        if (newString == null) {
            newString = "";
        }

        int i = mainString.lastIndexOf(oldString);

        if (i < 0) return mainString;

        StringBuilder mainSb = new StringBuilder(mainString);

        while (i >= 0) {
            mainSb.replace(i, i + oldString.length(), newString);
            i = mainString.lastIndexOf(oldString, i - 1);
        }
        return mainSb.toString();
    }

    /**
     * Formatta una stringa eliminando tutti i caratteri non stampabili e
     * tutti gli spazi iniziali e finali
     * @param mainString la stringa di input
     * @return mainString formattata
     */
    public static String formatString(String mainString) {
        if(mainString == null) {
            return null;
        }
        return mainString.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}\\n\\t]", "").trim();
    }

    /**
     * Ritorna la posizione dell'ultima occorrenza della stringa occ
     * @param str la stringa in cui cercare l'occorrenza
     * @param occ la stringa di cui cercare l'ultima posizione
     * @return l'ultima occorrenza della stringa occ
     */
    public static Integer lastIndexOf(String str, String occ) {
        if ((str == null) || (str.length() == 0) || (occ == null) || (occ.length() == 0)) {
            return null;
        }
        return  str.lastIndexOf(occ);
    }

    /**
     * Ritorna la sottostringa di str dalla posizione begin alla posizione end
     * @param str la stringa da cui ricavare la sottostringa
     * @param begin l'indice di inizio sottostringa
     * @param end l'indice di fine sottostringa
     * @return la sottostringa
     */
    public static String substring(String str, int begin, int end) {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        return str.substring(begin,end);
    }

    /**
     * Ritorna la stringa che prende in input splittata per il parametro separatore inserito
     * @param str la stringa da cui ricavare la lista di caratteri
     * @param sep la stringa separatore per cui viene splittata la stringa str
     * @return la stringa splittata
     */
    public static String[] split(String str, String sep) {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        return str.split("[" + sep + "]", 0);
    }


    public static int convertStringToNumber(String value){
        int ret = 0;
        try {
            ret = Integer.parseInt(value);
        }catch (NumberFormatException e){
           return 0;
        }
        return ret;
    }

}
