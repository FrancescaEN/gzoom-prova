import { CustomMethod } from "app/api/model/customMethod";
import { DataSource } from "app/api/model/dataSource";
import { Enumeration } from "app/api/model/enumeration";
import { GlAccount } from "app/api/model/glAccount";
import { GlAccountClass } from "app/api/model/glAccountClass";
import { GlAccountType } from "app/api/model/glAccountType";
import { GlFiscalType } from "app/api/model/glFiscalType";
import { GlResourceType } from "app/api/model/glResourceType";
import { Party } from "app/api/model/party";
import { PartyEx } from "app/api/model/partyEx";
import { PeriodType } from "app/api/model/period-type";
import { RoleType } from "app/api/model/role-type";
import { StatusItem } from "app/api/model/statusItem";
import { UomRange } from "app/api/model/uomRange";
import { UomRatingScale } from "app/api/model/uomRatingScale";
import { WorkEffortPurposeType } from "app/api/model/workEffortPurposeType";
import { Uom } from "app/api/model/uom";
import { orderBy } from "lodash";
import { SelectItem } from "primeng/api";
import { WorkEffortView } from "app/api/model/workEffortView";
import { WorkEffortType } from "app/api/model/workEffortType";
import { CustomTimePeriod } from "app/api/model/customTimePeriod";
import { WorkEffortMeasure } from "app/api/model/workEffortMeasure";
import { PartyRoleView } from "app/api/model/partyRoleView";
import { PartyRoleEx } from "app/api/model/partyRoleEx";
import { PartyRole } from "app/api/model/partyRole";

export function dropdownWorkEffortPurposeType(types: WorkEffortPurposeType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: WorkEffortPurposeType) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.workEffortPurposeTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownGlAccountType(types: GlAccountType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: GlAccountType) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.glAccountTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownGlResourceType(types: GlResourceType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: GlResourceType) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.glResourceTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownWorkEffortMeasure(types: WorkEffortMeasure[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: WorkEffortMeasure) => { return { label: ((!secondaryLang) ? x.uomDescr : x.uomDescrLang), value: x.workEffortMeasureId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownWorkEffortMeasureJoinWorkEffort(types: WorkEffortMeasure[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: WorkEffortMeasure) => { return { label: ((!secondaryLang) ? x.workEffort.workEffortName : x.workEffort.workEffortNameLang), value: x.workEffortMeasureId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownEnumeration(types: Enumeration[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: Enumeration) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.enumId } });
        return orderBy(selItem, ['label'], ['asc']); //enumeration ha il sequenceId
        return selItem
    }
    return []
}

export function dropdownPeriodType(types: PeriodType[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: PeriodType) => { return { label: x.description, value: x.periodTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownStatusItem(types: StatusItem[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: StatusItem) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.statusId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownUom(types: Uom[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: Uom) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.uomId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownUomRatingScale(types: UomRatingScale[], secondaryLang: boolean): SelectItem<{ uomId: string, uomRatingValue: number }>[] {
    if (types) {
        const selItem = types.map((x: UomRatingScale) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: { uomId: x.uomId, uomRatingValue: x.uomRatingValue } } });
        return selItem;
    }
    return []
}

export function dropdownUomRatingScaleByUomId(types: UomRatingScale[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: UomRatingScale) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.uomRatingValue } });
        return selItem;
    }
    return []
}

export function dropdownParty(types: Party[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: Party) => { return { label: ((!secondaryLang) ? x.partyName : x.partyNameLang), value: x.partyId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownPartyRoleEx(types: PartyRoleEx[], secondaryLang: boolean): SelectItem<Partial<PartyRole>>[] {
    if (types) {
        const selItem = types.map((x: PartyRoleEx) => {
            const { partyId, roleTypeId, party, roleType, partyParentRole } = x;
            const { description: roleTypeDescription, descriptionLang: roleTypeDescriptionLang } = roleType;
            const { partyName, partyNameLang } = party;
            const { parentRoleCode } = partyParentRole;
            return { label: ((!secondaryLang) ? `${parentRoleCode ?? ''} - ${partyName ?? ''} (${roleTypeDescription ?? ''})` : `${parentRoleCode ?? ''} - ${partyNameLang ?? ''} (${roleTypeDescriptionLang ?? ''})`), value: { partyId, roleTypeId } }
        });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownPartyEx(types: PartyEx[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: PartyEx) => { return { label: ((x.partyParentRole.parentRoleCode ? `${x.partyParentRole.parentRoleCode} - ` : '') + ((!secondaryLang) ? x.partyName : x.partyNameLang) ?? ''), value: x.partyId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownGlAccountClass(types: GlAccountClass[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: GlAccountClass) => { return { label: ((x.accountClassCode ? `${x.accountClassCode} - ` : '') + (x.description) ?? ''), value: x.glAccountClassId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownGlAccount(types: GlAccount[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: GlAccount) => { return { label: ((x.accountCode ? `${x.accountCode} - ` : '') + ((!secondaryLang) ? x.accountName : x.accountNameLang) ?? ''), value: x.glAccountId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownGlFiscalType(types: GlFiscalType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: GlFiscalType) => { return { label: (((!secondaryLang) ? x.description : x.descriptionLang) ?? ''), value: x.glFiscalTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownDataSource(types: DataSource[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: DataSource) => { return { label: x.description, value: x.dataSourceId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownWorkEffortView(types: WorkEffortView[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: WorkEffortView) => { return { label: (((!secondaryLang) ? x.workEffortName : x.workEffortNameLang) ?? ''), value: x.workEffortId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownCustomMethod(types: CustomMethod[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: CustomMethod) => { return { label: (x.customMethodId + " - " + x.description), value: x.customMethodId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownCustomTimePeriod(types: CustomTimePeriod[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: CustomTimePeriod) => { return { label: (((!secondaryLang) ? x.periodName : x.periodNameLang) ?? ''), value: x.customTimePeriodId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownPartyRoleView(types: PartyRoleView[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: PartyRoleView) => { return { label: x.partyName, value: x.partyId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownUomRange(types: UomRange[]): SelectItem[] {
    if (types) {
        const selItem = types.map((x: UomRange) => { return { label: x.description, value: x.uomRangeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownRoleType(types: RoleType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: RoleType) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.roleTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}

export function dropdownWorkEffortType(types: WorkEffortType[], secondaryLang: boolean): SelectItem[] {
    if (types) {
        const selItem = types.map((x: WorkEffortType) => { return { label: ((!secondaryLang) ? x.description : x.descriptionLang), value: x.workEffortTypeId } });
        return orderBy(selItem, ['label'], ['asc']);
    }
    return []
}