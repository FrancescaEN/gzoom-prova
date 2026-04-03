import { VarUtils } from '../../../utils/var-utils';

export class Attachment {
  id: string;
  fileName: string;
  description: string;
  info: string;
  fromDate: Date;
  thruDate: Date;

  static fromJson(src: any): Attachment {
    const tgt = new Attachment();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.contentId;
    tgt.fileName = src.contentName;
    tgt.description = src.description;
    tgt.info = src.objectInfo;
    tgt.fromDate = VarUtils.isDefined(src.fromDate) ? new Date(src.fromDate) : null;
    tgt.thruDate = VarUtils.isDefined(src.thruDate) ? new Date(src.thruDate) : null;

    return tgt;
  }
}
