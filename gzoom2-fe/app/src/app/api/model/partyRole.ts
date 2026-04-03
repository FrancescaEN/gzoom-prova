import { Party } from "./party";
import { RoleType } from "./role-type";

/**
 * Model of PartyRole
 */
export class PartyRole {
    partyId?: string;
    roleTypeId?: string;
    party?: Party;
    roleType?: RoleType;

}