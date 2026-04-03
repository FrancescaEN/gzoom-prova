import { SelectItem } from "primeng/api";

export interface DetectionMode {
    inputEnumId: string,
    detectOrgUnitIdFlag: string
}

export const detectionModes: SelectItem<DetectionMode>[] = [
    { label: "By single objective", value: { inputEnumId: "ACCINP_OBJ", detectOrgUnitIdFlag: "N" } },
    { label: "By organizational unit", value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "Y" } },
    { label: "Unique by indicator", value: { inputEnumId: "ACCINP_UO", detectOrgUnitIdFlag: "N" } },
]