import { VarUtils } from '../../../utils/var-utils';
import { GoalNote as GoalNoteDTO } from '../../../../api/service/goal-file/dto/note/goal-note';

export class Note {
  id: string;
  name: string;
  nameLang: string
  info: string;
  infoLang: string;
  sequenceId: number;

  static fromResponse(src: GoalNoteDTO): Note {
    const tgt = new Note();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.name = VarUtils.isDefined(src.name) ? src.name.trim() : src.name;
    tgt.nameLang = src.nameLang;
    tgt.info = VarUtils.isDefined(src.info) ? src.info.trim() : src.info;
    tgt.infoLang = src.infoLang;
    tgt.sequenceId = src.sequenceId;

    return tgt;
  }
}
