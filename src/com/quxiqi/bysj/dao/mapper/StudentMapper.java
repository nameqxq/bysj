package com.quxiqi.bysj.dao.mapper;

import java.util.List;

import com.quxiqi.bysj.bean.Student;

public interface StudentMapper {
	Student select(Student stu);
	List<Student> selectForLike(Student stu);
	void insertList(List<Student> stu);
}
