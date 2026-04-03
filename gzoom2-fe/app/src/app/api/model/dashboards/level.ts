import { Area } from "./area";
import { Process } from "./process";

export class Level {
    levelId: number;
    levelName?: string;
    levelNameLang?: string;
}

export interface ProcessLevel extends Level {
    processes?: Process[];
}

export interface AreaLevel extends Level {
    areas?: Area[];
}