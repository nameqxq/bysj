package com.quxiqi.bysj.bean;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.quxiqi.bysj.util.DateUtil;
@Alias("User")
public class User extends MyPage implements Comparable<User>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3667227605076017501L;
	//用户名，唯一，主键
	private String userName;
	//密码
	private String password;
	//手机号 ，唯一
	private String phoneNumber;
	//年龄
	private Integer age;
	//性别
	private String gender;//1为男，0为女 
	//邮件
	private String email;
	//用户类型 
	private String usertypes;
	//宿舍号 （楼号+层数+门牌号-->6#1#29）
	private Dormitory dormitory;
	//创建时间(时间戳)
	private String createDate;
	//关联学生信息
	private Student student;
	
	public User() {
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsertypes() {
		return usertypes;
	}
	
	public void setUsertypes(String usertypes) {
		this.usertypes = usertypes;
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
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = 
				DateUtil.toDateString(createDate, DateUtil.DEFAULT_DATETIME_PATTERN);
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	public void setStudentId(String studentId) {
		if(this.student == null);
			student = new Student();
		student.setId(studentId);	
	}

	@Override
	public int compareTo(User o) {
		return userName.compareTo(o.userName);
	}


	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", phoneNumber=" + phoneNumber + ", age=" + age
				+ ", gender=" + gender + ", email=" + email + ", usertypes=" + usertypes + ", dormitory=" + dormitory
				+ ", createDate=" + createDate + ", student=" + student + "]";
	}
	
	
	
}
