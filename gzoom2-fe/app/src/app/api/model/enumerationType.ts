/**
 * Model of EnumerationType
 */
export class EnumerationType {
  constructor(
    public enumTypeId?: string,
    public parentTypeId?: string,
    public hasTable?: string,
    public description?: string
  ) { }
}
