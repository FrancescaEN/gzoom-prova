/**
 * Model of a WorkEffortAnalysis.
 */
export class WorkEffortAnalysis {
  constructor(
    public workEffortAnalysisId?: string,
    public description?: string,
    public descriptionLang?: string,
    public workEffortId?: string,
    public comments?: string,
    public yearM4Prev?: string,
    public yearM3Prev?: string,
    public yearM2Prev?: string,
    public yearM1Prev?: string,
    public yearPrev?: string,
    public yearP1Prev?: string,
    public yearP2Prev?: string,
    public yearP3Prev?: string,
    public yearP4Prev?: string,
    public labelM4Prev?: string,
    public labelM3Prev?: string,
    public labelM2Prev?: string,
    public labelM1Prev?: string,
    public labelPrev?: string,
    public labelP1Prev?: string,
    public labelP2Prev?: string,
    public labelP3Prev?: string,
    public labelP4Prev?: string,
    public referenceDate?: Date,
    public workEffortTypeId?: string,
    public availabilityId?: string,
    public excludeValidity?: string,
    public typeBalanceScoreConId?: string,
    public typeBalanceScoreTarId?: string,
    public typeBalanceConsIndId?: string,
    public typeBalanceTarIndId?: string
  ) { }


}
