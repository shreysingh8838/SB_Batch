package com.demoDatajpa_Mapping_.Hibernate.demoDatajpa_Mapping_.Hibernate.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    // All the columns from CSV file, we want them in our DB
    @Id
    private long userID;
    private String firstname;
    private String lastname;
    private String jobid;
    private long salary;
    private long managerId;
    private long departmentID;


    // Constructors
    public User(){

    }

    public User(long userID, String firstname, String lastname, String jobid, long salary, long managerId, long departmentID) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.jobid = jobid;
        this.salary = salary;
        this.managerId = managerId;
        this.departmentID = departmentID;
    }



    // Getters and Setters
    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }
}
