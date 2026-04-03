export class WorkEffortView {
  public workEffortId?: string;
  public workEffortName?: string;
  public workEffortNameLang?: string;
  public weEtch?: string;
  public workEffortTypeId?: string;

  constructor(
    workEffortId?: string,
    workEffortName?: string,
    workEffortNameLang?: string,
    weEtch?: string,
    workEffortTypeId?: string
  ) {
    this.workEffortId = workEffortId;
    this.workEffortName = workEffortName;
    this.workEffortNameLang = workEffortNameLang;
    this.weEtch = weEtch;
    this.workEffortTypeId = workEffortTypeId
  }
}
