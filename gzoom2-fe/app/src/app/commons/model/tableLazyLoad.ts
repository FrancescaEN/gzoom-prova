import { TableLazyLoadEvent } from 'primeng/table';

export interface TableLazyLoad extends TableLazyLoadEvent {
    secondaryLang?: boolean;
}
