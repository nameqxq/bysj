package com.quxiqi.bysj.bean;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("Student")
public class Student extends MyPage implements Comparable<Student>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4671824880888353344L;
	private String id;
	private String name;
	private Integer age;
	private String personNumber;
	private String className;
	private String admissionDate;
	private Integer schoolYear;
	private String gender;
	private Dormitory dormitory;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber.toUpperCase();
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionData) {
		this.admissionDate = 
				DateUtil.toDateString(admissionData, DateUtil.DEFAULT_DATETIME_PATTERN);
	}
	public Integer getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Dormitory getDormitory() {
		return dormitory;
	}
	public void setDormitory(Dormitory dormitory) {
		this.dormitory = dormitory;
	}
	public void setDormitoryNumber(String dormitoryNumber) {
		if(dormitory == null)
			dormitory = new Dormitory();
		dormitory.setDormitoryNumber(dormitoryNumber);
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", personNumber=" + personNumber
				+ ", className=" + className + ", admissionData=" + admissionDate + ", schoolYear=" + schoolYear
				+ ", gender=" + gender + ", dormitory=" + dormitory + "]";
	}
	@Override
	public int compareTo(Student o) {
		return id.compareTo(o.id);
	}
	
}
