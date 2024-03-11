package com.gdtsSystem.service.Interface;

import com.gdtsSystem.entity.StudentInfo;

import java.util.ArrayList;
import java.util.HashMap;

public interface StudentInfoService {
    ArrayList<StudentInfo> getStudentInfos(String find);
    ArrayList<StudentInfo> getStudentInfos(HashMap m);
    boolean insertStudentInfo(StudentInfo studentInfo);
    boolean insertStudentInfo(HashMap m);
    boolean deleteStudentInfo(StudentInfo studentInfo);
    boolean deleteStudentInfo(HashMap m);
    boolean updateStudentInfo(StudentInfo studentInfo);
    boolean updateStudentInfo(HashMap m);
	HashMap getStudentInfos_2(HashMap m);
}
