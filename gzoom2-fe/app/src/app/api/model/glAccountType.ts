/**
 * Model of a GlAccountType.
 */
export class GlAccountType {
    constructor(
        public glAccountTypeId?: string,
        public description?: string,
        public descriptionLang?: string,
        public isReservedAccount?: string,
        public accountTypeEnumId?: string,
    ) { }
}
