import { Component, OnInit, ViewEncapsulation, WritableSignal, computed, effect, signal } from '@angular/core';
import { GoalService } from 'app/api/service/goal.service';
import { Goal } from '../../commons/model/goal-file/Goal';
import { Indicator } from '../../commons/model/goal-file/indicator/Indicator';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { StringUtils } from '../../commons/utils/string-utils';
import { VarUtils } from '../../commons/utils/var-utils';
import { AccountCode } from '../../commons/enum/AccountCode';
import { GoalTypeId } from '../../commons/enum/GoalTypeId';
import { UnitOfMeasurementId } from '../../commons/enum/UnitOfMeasurementId';
import { CommentModalComponent } from './modal/comment-modal/comment-modal.component';
import { AttachmentModalComponent } from './modal/attachment-modal/attachment-modal.component';
import { GoalStatusId } from '../../commons/model/goal-file/status/goal-status-id';
import { PortalPermission } from './portal-permissions';

@Component({
  selector: 'gzoom-portal',
  templateUrl: './gzoom-portal.component.html',
  styleUrl: './gzoom-portal.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class GzoomPortalComponent implements OnInit {
  protected goals: Goal[] = [];
  protected selectedGoalId: WritableSignal<string> = signal<string>('');
  protected goalRoot: WritableSignal<Goal> = signal<Goal>(null);

  protected commentModal: DynamicDialogRef | undefined;
  protected attachmentModal: DynamicDialogRef | undefined;

  protected statusErrorMessages: { [key in GoalStatusId]?: string } = {
    [GoalStatusId.ToBeAssigned]: 'PORTAL_GOAL_TO_BE_ASSIGNED_ERROR',
    [GoalStatusId.InvestigativePhase]: 'PORTAL_GOAL_INVESTIGATIVE_PHASE_ERROR',
    [GoalStatusId.EvaluationPhase]: 'PORTAL_GOAL_EVALUATION_PHASE_ERROR',
    [GoalStatusId.EvaluationCompleted]: 'PORTAL_GOAL_EVALUATION_COMPLETED_ERROR',
    [GoalStatusId.ContradictoryProcess]: 'PORTAL_GOAL_CONTRADICTORY_DEVELOPMENT_ERROR',
    [GoalStatusId.Conciliation]: 'PORTAL_CONCILIATION_ERROR'
  }

  protected hasPermission = computed(() =>
    PortalPermission.generatePortalPermissions(this.goalRoot()?.goalStatus.id));

  constructor(private readonly dialogService: DialogService,
              private readonly goalService: GoalService) {
    effect(() => {
      if (StringUtils.notEmpty(this.selectedGoalId())) {
        this.loadGoal();
      }
    });
  }

  ngOnInit(): void {
    this.collapseSidebarFun()

    this.goalService.getGoalRoots().subscribe({
      next: data => {
        // sorting by start date desc
        this.goals = data.sort((a, b) => b.goalPeriod.fromDate.getTime() - a.goalPeriod.fromDate.getTime());
        this.selectedGoalId.set(this.goals[0].id); // triggers loadGoal
      },
      error: error => {
        console.error('Could not get goals:', error.message);
      }
    })
  }

  private loadGoal(): void {
    this.goalService.getGoals(this.selectedGoalId()).subscribe({
      next: data => {
        this.goalRoot.set(this.applyNoteNameReplacement(data)[0]);

        if (Object.keys(this.statusErrorMessages).includes(this.goalRoot().goalStatus.id)) {
          return;
        }

        this.goalRoot()?.children
          .filter(section => section.goalType.id === GoalTypeId.MEASUREMENT)
          .map(section => {
            section.children?.map(goal => {
              goal.children?.forEach(subGoal => {
                this.goalService.getNotes(subGoal.id).subscribe(comments => {
                  subGoal.notes = comments.filter(note => note.name === 'Evidenze (note)' && (note.info?.length > 0));
                });

                this.goalService.getAttachments(subGoal.id).subscribe(attachments => {
                  subGoal.attachments = attachments;
                });

                subGoal.indicators.forEach(indicator => {
                  indicator.score = indicator.value?.filter(v => v.scoreKpi)[0]?.amount;
                  indicator.scoreCalculated = (VarUtils.isDefined(subGoal.objWeight)
                    && VarUtils.isDefined(indicator.kpiScoreWeight) && VarUtils.isDefined(indicator.score))
                    ? (subGoal.objWeight * indicator.kpiScoreWeight / 100 * indicator.score) : null;
                })
              })
            })
          });
      },
      error: error => {
        console.error('Could not get goal tree:', error.message);
      }
    })
  }

  private loadComment(goalId: string): void {
    this.goalRoot()?.children
      .filter(section => section.goalType.id === GoalTypeId.MEASUREMENT)
      .map(section => {
        section.children?.map(goal => {
          goal.children?.filter(subGoal => subGoal.id === goalId)
            .forEach(subGoal => {
              this.goalService.getNotes(subGoal.id).subscribe(comments => {
                subGoal.notes = comments.filter(note => note.name === 'Evidenze (note)' && (note.info?.length > 0));
              });
            })
        })
      });
  }

  private loadAttachment(goalId: string): void {
    this.goalRoot()?.children
      .filter(section => section.goalType.id === GoalTypeId.MEASUREMENT)
      .map(section => {
        section.children?.map(goal => {
          goal.children?.filter(subGoal => subGoal.id === goalId)
            .forEach(subGoal => {
              this.goalService.getAttachments(subGoal.id).subscribe(attachments => {
                subGoal.attachments = attachments;
              });
            })
        })
      });
  }

  applyNoteNameReplacement(nodes: Goal[]): Goal[] {
    nodes.forEach(node => {
      // Replace node.name if a matching note is found
      const specificNote = node.notes?.find(note => note.name === 'Obiettivo specifico');
      if (specificNote && StringUtils.notEmpty(specificNote.info)) {
        node.name = specificNote.info;
      }

      // Recurse into children if they exist
      if (node.children?.length) {
        this.applyNoteNameReplacement(node.children);
      }
    });

    return nodes;
  }

  extractTotalResult(goal: Goal): number {
    return goal?.indicators?.filter(i => i.accountCode === AccountCode.SCORE)[0]?.value[0]?.amount;
  }

  extractAchievedResult(indicator: Indicator): number | string {
    const value = indicator.value?.filter(v => !v.scoreKpi)[0];
    return value ? (value.valueUomId === UnitOfMeasurementId.SN ? value.goalIndicator?.valueCode : value.amount) : null;
  }

  collapseSidebarFun() {
    const dom: any = document.querySelector('body');
    const menu: any = document.querySelector('#sidebar');
    dom.classList.add('push-right');
    menu.classList.add('collapse');
  }

  asGoal(value: Goal): Goal {
    return value;
  }

  asIndicator(value: Indicator): Indicator {
    return value;
  }

  showCommentModal(goal: Goal) {
    this.commentModal = this.dialogService.open(CommentModalComponent, {
      data: {
        goal: goal
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });

    this.commentModal.onClose.subscribe((_) => {
      this.loadComment(goal.id);
    });
  }

  showAttachmentModal(goal: Goal) {
    this.attachmentModal = this.dialogService.open(AttachmentModalComponent, {
      data: {
        goal: goal
      },
      styleClass: 'gzoom-modal gzoom-portal'
    });

    this.attachmentModal.onClose.subscribe((_) => {
      this.loadAttachment(goal.id);
    });
  }

  protected readonly GoalTypeId = GoalTypeId;
  protected readonly isDefined = VarUtils.isDefined;
  protected readonly PortalPermission = PortalPermission;
}



