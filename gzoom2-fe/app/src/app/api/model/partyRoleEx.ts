import { Party, PartyParentRole } from "./party";
import { PartyRole } from "./partyRole";
import { RoleType } from "./role-type";

export class PartyRoleEx extends PartyRole {
    public roleType?: RoleType;
    public party?: Party;
    public partyParentRole?: PartyParentRole;
}