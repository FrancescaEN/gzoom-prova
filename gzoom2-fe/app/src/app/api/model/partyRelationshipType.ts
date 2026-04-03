export class PartyRelationshipType {
    constructor(
        public partyRelationshipTypeId: string,
        public partyRelationshipName: string,
        public parentTypeId?: string,
        public description?: string,
        public explicitRole?: string
    ) { }
}