import { Area } from "./area";
import { Factor } from "./factor";
import { MeasureType } from "./measureType";
import { Process } from "./process";

export class Period {
    year: number;
}

export class PeriodArea extends Period {
    areas?: Area[]
}

export class PeriodProcess extends Period {
    processes?: Process[]
}

export class PeriodFactor extends Period {
    factors?: Factor[]
}

export class PeriodMeasureType extends Period {
    measureTypes?: MeasureType[]
}