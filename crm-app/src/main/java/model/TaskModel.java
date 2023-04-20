package model;

import java.sql.Date;

public class TaskModel {

    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private ProjectModel projectModel;
    private StatusModel statusModel;
    private UserModel userModel;

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
