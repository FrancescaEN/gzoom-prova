package it.mapsgroup.gzoom.util;

import it.mapsgroup.gzoom.common.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Function;

public class GenericTreeBuilder<E, K, D extends TreeNode<D>> {
    private final Function<E, K> idExtractor;
    private final Function<E, K> parentIdExtractor;
    private final Function<E, D> entityToEntityMapper;

    public GenericTreeBuilder(Function<E, K> idExtractor,
                              Function<E, K> parentIdExtractor,
                              Function<E, D> entityToEntityMapper) {
        this.idExtractor = idExtractor;
        this.parentIdExtractor = parentIdExtractor;
        this.entityToEntityMapper = entityToEntityMapper;
    }

    public List<D> buildTree(List<E> flatList) {
        Map<K, D> entityMap = new HashMap<>();
        List<D> roots = new ArrayList<>();

        for (E entity: flatList) {
            D child = entityToEntityMapper.apply(entity);
            entityMap.put(idExtractor.apply(entity), child);
        }

        for (E entity: flatList) {
            K parentId = parentIdExtractor.apply(entity);
            D child = entityMap.get(idExtractor.apply(entity));

            if (parentId == null) {
                roots.add(child);
            } else {
                D parent = entityMap.get(parentId);
                if (parent != null) {
                    parent.addChild(child);
                }
            }
        }

        return roots;
    }
}
