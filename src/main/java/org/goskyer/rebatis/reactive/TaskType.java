package org.goskyer.rebatis.reactive;

public enum TaskType {

    Insert("Insert"), Delete("Delete"), Update("Update"), Select("Select");

    private String type;

    TaskType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
