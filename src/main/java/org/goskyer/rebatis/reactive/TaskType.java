package org.goskyer.rebatis.reactive;

/**
 * @author Galaxy
 * @description TODO
 * @since 2019-05-26 10:10
 */
public enum TaskType {

    /**
     * Insert
     */
    Insert("Insert"),
    /**
     * Delete
     */
    Delete("Delete"),
    /**
     * Update
     */
    Update("Update"),
    /**
     * Select
     */
    Select("Select");

    private String type;

    TaskType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
