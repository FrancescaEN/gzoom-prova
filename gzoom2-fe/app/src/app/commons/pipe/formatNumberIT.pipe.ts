import {Pipe, PipeTransform} from '@angular/core';
import {isDefined} from "../model/commons";
import {isNaN} from "lodash";

@Pipe({
    name: 'formatNumberIT'
})
export class FormatNumberITPipe implements PipeTransform {
    /**
     * Converts a number or a string containing a number into Italian representation:
     * - comma as decimals separator
     * - every 3 digits of the integer part separated with a full stop
     *
     * @param value       Input value
     * @param maxDecimals Maximum number of decimal digits. All eventual zeroes will be trimmed
     * @return A formatted number or unmodified {{value}} if it's not a valid number
     */
    transform(value: string | number, maxDecimals?: number): string {
        if (!isDefined(value)) return null;
        if ((typeof value === 'string') && isNaN(Number(value))) return value;

        if (isDefined(maxDecimals) && maxDecimals > 0) {
          value = Number(value).toFixed(maxDecimals);
        }

        // Converti il numero in una stringa per manipolarlo
        let numStr = value.toString();
        let segno = null;
        if (numStr[0] == "-") {
            segno = "-";
            numStr = numStr.substring(1);
        }

        // Dividi la parte intera e la parte decimale (se esiste)
        const parts = numStr.split(".");
        let integerPart = parts[0];
        let decimalPart = parts[1] || '';
        while (decimalPart.endsWith('0')) {
          decimalPart = decimalPart.substring(0, decimalPart.length - 1);
        }

        // Aggiungi il separatore delle migliaia
        const thousandsSeparator = ".";
        for (let i = integerPart.length - 3; i > 0; i -= 3) {
            integerPart = integerPart.slice(0, i) + thousandsSeparator + integerPart.slice(i);
        }

        // Unisci la parte intera e la parte decimale con la virgola come separatore
        let formattedNumber = decimalPart
            ? integerPart + "," + decimalPart
            : integerPart;

        if (segno) formattedNumber = segno + formattedNumber;
        return formattedNumber;
    }
}
