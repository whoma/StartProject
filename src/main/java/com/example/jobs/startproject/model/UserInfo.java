package com.example.jobs.startproject.model;

/**
 * Created by jobs on 2016/10/15.
 */

public class UserInfo {
    //培养方案
    private String major_plan;
    //课程号
    private String course_id;
    //课程名
    private String course_name;
    //课序号
    private String course_number;
    //学分
    private String course_grades;
    //课程属性
    private String course_attribute;
    //考试类型
    private String exam_type;
    //教师
    private String teach_name;
    //修读方式
    private String study_type;
    //选课状态
    private String course_status;
    //周次
    private String study_time;


    //星期
    private String study_week;
    //节次
    private String course_period;
    //节数
    private String course_times;
    //校区
    private String study_place;
    //教学楼
    private String course_building;
    //教室
    private String course_classroom;

    public String getStudy_week() {
        return study_week;
    }

    public void setStudy_week(String study_week) {
        this.study_week = isSingString(study_week);
    }


    public String getMajor_plan() {
        return major_plan;
    }

    public void setMajor_plan(String major_plan) {
        this.major_plan = isSingString(major_plan);
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = isSingString(course_name);
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = isSingString(course_id);
    }

    public String getCourse_number() {
        return course_number;
    }

    public void setCourse_number(String course_number) {
        this.course_number = isSingString(course_number);
    }

    public String getCourse_grades() {
        return course_grades;
    }

    public void setCourse_grades(String course_grades) {
        this.course_grades = isSingString(course_grades);
    }

    public String getCourse_attribute() {
        return course_attribute;
    }

    public void setCourse_attribute(String course_attribute) {
        this.course_attribute = isSingString(course_attribute);
    }

    public String getStudy_type() {
        return study_type;
    }

    public void setStudy_type(String study_type) {
        this.study_type = isSingString(study_type);
    }

    public String getStudy_time() {
        return study_time;
    }

    public void setStudy_time(String study_time) {
        this.study_time = isSingString(study_time);
    }

    public String getCourse_status() {
        return course_status;
    }

    public void setCourse_status(String course_status) {
        this.course_status = isSingString(course_status);
    }

    public void setCourse_status2(String course_status) {
        this.course_status = course_status;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = isSingString(exam_type);
    }

    public String getTeach_name() {
        return teach_name;
    }

    public void setTeach_name(String teach_name) {
        this.teach_name = isSingString(teach_name);
    }

    public String getCourse_period() {
        return course_period;
    }

    public void setCourse_period(String course_period) {
        this.course_period = isSingString(course_period);
    }

    public String getCourse_times() {
        return course_times;
    }

    public void setCourse_times(String course_times) {
        this.course_times = isSingString(course_times);
    }

    public String getCourse_building() {
        return course_building;
    }

    public void setCourse_building(String course_building) {
        this.course_building = isSingString(course_building);
    }

    public String getStudy_place() {
        return study_place;
    }

    public void setStudy_place(String study_place) {
        this.study_place = isSingString(study_place);
    }

    public String getCourse_classroom() {
        return course_classroom;
    }

    public void setCourse_classroom(String course_classroom) {
        this.course_classroom = isSingString(course_classroom);
    }



    @Override
    public String toString() {
        return "培养方案:\t" + major_plan +
                "\n课程号:\t" + course_id +
                "\n课程名:\t" + course_name +
                "\n课序号:\t" + course_number +
                "\n学分:\t" + course_grades +
                "\n课程属性:\t" + course_attribute +
                "\n考试类型:\t" + exam_type +
                "\n教师:\t" + teach_name +
                "\n修读方式:\t" + study_type +
                "\n选课状态:\t" + course_status +
                "\n周次:\t" + study_time +
                "\n星期:\t" + study_week +
                "\n节次:\t" + course_period +
                "\n节数:\t" + course_times +
                "\n校区:\t" + study_place +
                "\n教学楼:\t" + course_building +
                "\n教室:\t" + course_classroom;
    }

    public static String isSingString(String data) {
        if (data.contains(" ")) {
            int index = data.lastIndexOf(" ");
            return data.substring(index + 1);
        } else {
            return data;
        }
    }
}
