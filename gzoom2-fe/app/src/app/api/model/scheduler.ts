export class ServiceJob {
    services: ServiceJobInfo[];
}

export class ServiceJobInfo {
    key: string;
    name: string;
    className: string;
    parameters: ParameterJob[];
}

export class ParameterJob {
    key: string
    name: string;
    type: string;
    defaultValue: any;
    required: boolean | false;
}

export class JobData {
    callbackObject: { [key: string]: any }
    startDate: Date;
    endDate: Date;
    frequency: Frequency;
    cronExpression: string;
}

export enum Frequency {
    NO_REPEAT = "NO_REPEAT",
    HOUR = "HOUR",
    DAY = "DAY",
    WEEK = "WEEK",
    MONTH = "MONTH",
    YEAR = "YEAR",
    CUSTOM = "CUSTOM"
}