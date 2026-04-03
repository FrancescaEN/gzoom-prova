package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Any response that includes several results as a list uses this bean.
 *
 * @author Andrea Fossi.
 */
public class Result<T> {
    private List<T> results;
    private int total;

    public Result(List<T> results, int total) {
        this.results = results;
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
