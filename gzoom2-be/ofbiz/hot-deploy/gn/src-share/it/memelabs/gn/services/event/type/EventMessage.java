package it.memelabs.gn.services.event.type;

import it.memelabs.gn.services.event.EventMessageTypeOfBiz;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 10/01/14
 *
 * @author Andrea Fossi
 */
public abstract class EventMessage extends HashMap<String, Object> {

    public EventMessage() {
        setType(getType());
        this.put("timestamp", null);
    }

    public void setId(long id) {
        this.put("id", id);
    }

    public void setType(EventMessageTypeOfBiz kind) {
        this.put("type", (kind != null) ? kind.toString() : null);
    }

    public void setSender(String sender) {
        this.put("sender", sender);
    }

    public void setTimestamp(long timestamp) {
        this.put("timestamp", timestamp);
    }

    public abstract EventMessageTypeOfBiz getType();

    @Override
    public Object get(Object key) {
        return "type".equals(key) ? getType() : super.get(key);
    }

    public Long getId() {
        return (Long) this.get("id");
    }

    public String getSender() {
        return (String) this.get("sender");
    }

    public Long getTimestamp() {
        return (Long) this.get("timestamp");
    }


    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> checkMap(Object object) {
        if (object != null && !(object instanceof Map)) throw new ClassCastException("Not a map");
        return (Map<K, V>) object;
    }


    @SuppressWarnings("unchecked")
    public static <T> List<T> checkList(Object object) {
        return (List<T>) checkCollectionCast(object, List.class);
    }

    private static <C extends Collection<?>> C checkCollectionCast(Object object, Class<C> clz) {
        return clz.cast(object);
    }

    /**
     * Assuming outerMap not null; if null will throw a NullPointerException
     */
    public static <K, IK, V> Map<IK, V> getMapFromMap(Map<K, Object> outerMap, K key) {
        Map<IK, V> innerMap = checkMap(outerMap.get(key));
        if (innerMap == null) {
            innerMap = new HashMap<IK, V>();
            outerMap.put(key, innerMap);
        }
        return innerMap;
    }

}
