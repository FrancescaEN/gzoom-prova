import { Party, PartyParentRole } from "./party";
import { PartyRole } from "./partyRole";

export class PartyEx extends Party {
    partyParentRole: PartyParentRole;
    partyRole: PartyRole;
}