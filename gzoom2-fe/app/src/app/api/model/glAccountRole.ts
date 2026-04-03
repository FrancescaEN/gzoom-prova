import { Party } from "./party";
import { RoleType } from "./role-type";

export class GlAccountRole {
    glAccountId: string;
    partyId: string;
    party?: Party;
    roleTypeId: string;
    roleType?: RoleType;
    fromDate: Date;
    thruDate?: Date;
}