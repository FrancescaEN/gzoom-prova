import { PipeTransform, Pipe } from '@angular/core';

@Pipe({
    name: 'formatPercentNumberStyle'
})
export class FormatPercentNumberStylePipe implements PipeTransform {
    transform(value: number): string {
        if (value === null || value === undefined || isNaN(value)) {
            return "0 %";
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
        const decimalPart = parts[1] || "";
    
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
        return formattedNumber + " %";
    }
}
