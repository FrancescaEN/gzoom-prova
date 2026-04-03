package it.mapsgroup.gzoom.entity.goalfile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoalFileTypeNode {
    private GoalFileType child;
    private GoalFileType father;

    public String getChildId() {
        return child != null ? child.getId() : null;
    }

    public GoalFileTypeNode setChild(GoalFileType child) {
        this.child = child;
        return this;
    }

    public String getFatherId() {
        return father != null ? father.getId() : null;
    }

    public GoalFileTypeNode setFather(GoalFileType father) {
        this.father = father;
        return this;
    }

    public GoalFileTypeNode copy() {
        return new GoalFileTypeNode()
                .setFather(father)
                .setChild(child);
    }
}
