package com.kinomisfit.recyclocator.Models;

public class GoalsModel {

    public GoalsModel() {}

    String title;
    String type;
    String desc;
    String goalID;
    String target;

    public GoalsModel(String title, String type, String desc, String goalID, String target) {
        this.title = title;
        this.type = type;
        this.desc = desc;
        this.goalID = goalID;
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGoalID() {
        return goalID;
    }

    public void setGoalID(String goalID) {
        this.goalID = goalID;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
