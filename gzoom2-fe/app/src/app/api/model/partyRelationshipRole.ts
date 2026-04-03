export class PartyRelationshipRole {
    constructor(
        public partyRelationshipTypeId: string,
        public roleTypeValidFrom: string,
        public roleTypeValidTo: string,
        public informativeSequence?: string
    ) { }
}