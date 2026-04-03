package it.mapsgroup.gzoom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permission data structure.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Permissions {
    private Map<String, List<String>> permissions;

    public Permissions(Map<String, List<String>> permissions) {
        this.permissions = permissions;
    }

    public Permissions() {
        permissions = new HashMap<>();
    }

    public void addPermission(String root, String action) {
        if (permissions.containsKey(root)) {
            List<String> list = permissions.get(root);
            if (!list.contains(action)) {
                permissions.get(root).add(action);
            }
        } else {
            List<String> lista = new ArrayList<>();
            lista.add(action);
            permissions.put(root, lista);
        }
    }
    public Map<String, List<String>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, List<String>> permissions) {
        this.permissions = permissions;
    }

    
}
