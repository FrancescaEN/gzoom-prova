package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.common.TreeNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GoalFileType extends GoalType implements TreeNode<GoalFileType> {
    private List<GoalFileType> children;
    private String icon;

    @Override
    public GoalFileType setId(String id) {
        super.setId(id);
        return this;
    }

    public GoalFileType setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    public GoalFileType setDescriptionLang(String descriptionLang) {
        super.setDescriptionLang(descriptionLang);
        return this;
    }

    @Override
    public void addChild(GoalFileType child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public GoalFileType addChildren(List<GoalFileType> childList) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.addAll(childList);
        return this;
    }

    public GoalFileType setIcon(String icon) {
        this.icon = icon;
        return this;
    }
}
