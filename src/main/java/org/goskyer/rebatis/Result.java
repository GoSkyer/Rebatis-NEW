package org.goskyer.rebatis;

import org.goskyer.rebatis.convert.RowMap;
import org.goskyer.rebatis.reactive.TaskType;

import java.util.List;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-05-26 12:16
 **/
public class Result {

    private TaskType type;

    private Throwable throwable;

    private int effectedRows;

    private List<RowMap<String, Object>> raws;

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public int getEffectedRows() {
        return effectedRows;
    }

    public void setEffectedRows(int effectedRows) {
        this.effectedRows = effectedRows;
    }

    public List<RowMap<String, Object>> getRaws() {
        return raws;
    }

    public void setRaws(List<RowMap<String, Object>> raws) {
        this.raws = raws;
    }

    public boolean isSucceeded() {
        return throwable == null;
    }

    public boolean isNon() {
        return effectedRows != raws.size();
    }

}
