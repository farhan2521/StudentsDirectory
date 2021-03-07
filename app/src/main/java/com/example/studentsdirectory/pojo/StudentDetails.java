package com.example.studentsdirectory.pojo;

public class StudentDetails {
    private int id;
    private String studentName;
    private String studentDOB;
    private String studentDepartment;

    public StudentDetails(int id, String name, String dob, String department) {
        this.id = id;
        this.studentName = name;
        this.studentDOB = dob;
        this.studentDepartment = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentDOB() {
        return studentDOB;
    }

    public void setStudentDOB(String studentDOB) {
        this.studentDOB = studentDOB;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }
}
