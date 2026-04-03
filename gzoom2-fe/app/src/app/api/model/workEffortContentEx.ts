import { Content } from "./content";
import { DataResource } from "./dataResource";
import { WorkEffortContent } from "./workEffortContent";
import { WorkEffortContentType } from "./workEffortContentType";
import { WorkEffortView } from "./workEffortView";

export class WorkEffortContentEx extends WorkEffortContent {
    public content?: Content;
    public dataResource?: DataResource;
    public workEffortContentType?: WorkEffortContentType
    public workEffortView?: WorkEffortView

    constructor(
        public workEffortId?: string,
        public contentId?: string,
        public workEffortContentTypeId?: string,
        public fromDate?: Date,
        public thruDate?: Date,

        public contentTypeId?: string,
        public statusId?: string,
        public description?: string,
        public descriptionLang?: string,
        public contentName?: string,
        public mimeTypeId?: string,
        public objectInfo?: string,

        public dataResourceId?: string,
        public dataResourceTypeId?: string,
        public dataTemplateTypeId?: string,
        public dataResourceStatusId?: string,
        public dataResourceName?: string,

        public workEffortNameLang?: string,
        public workEffortName?: string
    ) {
        super(
            workEffortId,
            contentId,
            workEffortContentTypeId,
            fromDate,
            thruDate
        );
        this.content = new Content(
            contentId,
            contentTypeId,
            statusId,
            description,
            descriptionLang,
            contentName,
            mimeTypeId,
            objectInfo
        );

        this.dataResource = new DataResource(
            dataResourceId,
            dataResourceTypeId,
            dataTemplateTypeId,
            dataResourceStatusId,
            dataResourceName,
            mimeTypeId,
            objectInfo
        );

        this.workEffortView = new WorkEffortView(
            workEffortId,
            workEffortName,
            workEffortNameLang,
        )
    }
}