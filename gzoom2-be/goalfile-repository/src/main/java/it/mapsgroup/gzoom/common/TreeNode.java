package it.mapsgroup.gzoom.common;

import java.util.List;

public interface TreeNode<T extends TreeNode<T>> {
    List<T> getChildren();
    void addChild(T child);
}
