package com.mapsengineering.base.birt.util;


import org.slf4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

public class UtilNumber {
	private static final Logger LOG = getLogger(UtilNumber.class);

	private static int concatRomanToken(StringBuffer roman, int number, int tokenValue, String romanToken){
		int n = number;		
		while(n >= tokenValue){
            roman.append(romanToken);
            n -= tokenValue;
        }
		return n;
	}

	public static String convertNumberToRomanNumeral(int num){
		StringBuffer roman = new StringBuffer();
		int number = num;
		
		if(num <= 0 || num >=3999){
			return "";
		}
        
		number = concatRomanToken(roman, number, 1000, "M");
		number = concatRomanToken(roman, number, 900, "CM");
        number = concatRomanToken(roman, number, 500, "D");
        number = concatRomanToken(roman, number, 400, "CD");
        number = concatRomanToken(roman, number, 100, "C");
        number = concatRomanToken(roman, number, 90, "XC");
        number = concatRomanToken(roman, number, 50, "L");
        number = concatRomanToken(roman, number, 40, "XL");
        number = concatRomanToken(roman, number, 10, "X");
        number = concatRomanToken(roman, number, 9, "IX");
        number = concatRomanToken(roman, number, 5, "V");
        number = concatRomanToken(roman, number, 4, "IV");
        number = concatRomanToken(roman, number, 1, "I");
        
        return roman.toString();
	}

	/**
	 * Restituisce un boolean a seconda che una stringa sia un numero o no.
	 * Es:  123 -> true
	 * 	abdc123 -> false
	 * 	123,000 -> true
	 * 	123.000 -> true
	 * 	123.000.000 -> false
	 * @param str la stringa di input
	 * @return true o false
	 */
	public static Boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}

		try {
			Double.parseDouble(str.replace(",", "."));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Float sumAssocWeightNumber(Float a, Float b, Float c) {			
		return a + b - c;
	}
	
	public static String getFormatPattern(Integer decimalScale, String um){
		return getFormatPattern(decimalScale, um, "0");
    }
	
	public static String getFormatPatternWithoutZero(Integer decimalScale, String um){
		return getFormatPattern(decimalScale, um, "#");
	}
	
	public static String getFormatPattern(Integer decimalScale, String um, String patt) {
		String pattern = "#,##0";
		if (decimalScale != null && decimalScale > 0) {
			pattern = pattern + ".";
			
			 for(int i=0; i < decimalScale; i++ ){
				pattern += patt;
			}
		}
		if ("%".equals(um) || (um != null && um.indexOf("Perc") != -1)) {
			pattern = pattern + "%";
		}
		return pattern;
	}

	public static String getValueFormatted(String value, String pattern) {
		if (value == null) {
			return "";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
		DecimalFormat commaFormat = (DecimalFormat) nf;
		commaFormat.applyPattern(pattern);

		BigDecimal valueObject = new BigDecimal(value);
		if (pattern.indexOf("%") != -1) {
			valueObject = valueObject.divide(new BigDecimal(100));
		}
		return commaFormat.format(valueObject);
	}
}
