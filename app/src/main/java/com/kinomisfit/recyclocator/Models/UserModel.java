package com.kinomisfit.recyclocator.Models;

public class UserModel {

    public UserModel() {

    }

    String rewards;
    String dumps;


    public UserModel(String rewards, String dumps) {
        this.rewards = rewards;
        this.dumps = dumps;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getDumps() {
        return dumps;
    }

    public void setDumps(String dumps) {
        this.dumps = dumps;
    }
}
