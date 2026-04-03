export function getDate(date: Date): string {
  return (date) ? date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() : null;
}

export function addDaysFromGiulianDate(days: number): Date {
  if (!!days) {
    const giulianDate = new Date(1899, 11, 31, 0, 0, 0, 0);
    giulianDate.setDate(giulianDate.getDate() + days);
    return giulianDate;
  } else return null;

}

export function fromDateGetDaysFromGiulianDate(date: Date): number {
  if (date) {
    date = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0);
    const giulianDateStart = new Date(1899, 11, 31, 0, 0, 0, 0);
    const millisecondsPerDay = 24 * 60 * 60 * 1000;
    const timeDifference = date.getTime() - giulianDateStart.getTime(); // Differenza in millisecondi
    const daysDifference = timeDifference / millisecondsPerDay;
    return Math.trunc(daysDifference + 1); //Nel contesto delle date Giuliane modificate (spesso usate nei fogli di calcolo come Excel), il 1 gennaio 1900 è considerato come giorno 1. Tuttavia, poiché l'epoca di riferimento è il 31 dicembre 1899, per ottenere una differenza coerente con questa convenzione, è necessario aggiungere uno.
  }
  return null;
}

// Funzione per normalizzare la data a mezzanotte
// function normalizeToMidnight(date: Date): Date {
//   const normalizedDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
//   return normalizedDate;
// }

// Funzione per calcolare la differenza in giorni tra due date
// function calculateDateDifferenceInDays(date1: Date, date2: Date): number {
//   const normalizedDate1 = normalizeToMidnight(date1);
//   const normalizedDate2 = normalizeToMidnight(date2);
//
//   const millisecondsPerDay = 24 * 60 * 60 * 1000;
//   const timeDifference = normalizedDate2.getTime() - normalizedDate1.getTime();
//   return timeDifference / millisecondsPerDay;
// }

export function setDateUtc(date: Date): Date {
  return (!!date) ? new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0)) : null;
}
