import { Content } from "./content";
import { DataResource } from "./dataResource";

export class ContentExDataResource extends Content {
    public dataResource?: DataResource;
    public imageBase64?: string;
}