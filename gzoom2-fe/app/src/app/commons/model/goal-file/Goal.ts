import { ArrayUtils } from '../../utils/array-utils';
import { VarUtils } from '../../utils/var-utils';
import { GoalPeriod } from './GoalPeriod';
import { Indicator } from './indicator/Indicator';
import { GoalType } from './type/GoalType';
import { Note } from './note/Note';
import { AccountCode } from '../../enum/AccountCode';
import { Attachment } from './content/Attachment';
import { GoalStatus } from './status/goal-status';

export class Goal {
  id: string;
  name: string;
  goalType: GoalType;
  goalStatus: GoalStatus;
  goalPeriod: GoalPeriod;
  fromDate: Date;
  thruDate: Date;
  objWeight?: number;
  children: Goal[];
  indicators: Indicator[];
  notes: Note[];
  attachments: Attachment[];

  static fromJson(src: any): Goal {
    const tgt = new Goal();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.name = src.name;
    tgt.goalType = GoalType.fromJson(src.goalType);
    tgt.goalStatus = GoalStatus.fromGoalStatus(src.goalStatus);
    tgt.goalPeriod = GoalPeriod.fromJson(src.goalPeriod);
    tgt.fromDate = VarUtils.isDefined(src.fromDate) ? new Date(src.fromDate) : null;
    tgt.thruDate = VarUtils.isDefined(src.thruDate) ? new Date(src.thruDate) : null;
    tgt.objWeight = src.objWeight;
    tgt.children = ArrayUtils.notEmpty(src.children) ? src.children.map((g: any) => Goal.fromJson(g)) : [];
    tgt.indicators = ArrayUtils.notEmpty(src.indicators) ? src.indicators.map((g: any) => Indicator.fromJson(g)) : [];
    tgt.notes = ArrayUtils.notEmpty(src.notes) ? src.notes.map((g: any) => Note.fromResponse(g)) : [];

    return tgt;
  }

  getGoalIndicators(): Indicator[] {
    return this.indicators?.filter(i => i.accountCode !== AccountCode.SCORE);
  }
}
