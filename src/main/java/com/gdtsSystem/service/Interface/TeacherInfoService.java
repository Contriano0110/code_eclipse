package com.gdtsSystem.service.Interface;

import com.gdtsSystem.entity.StudentInfo;
import com.gdtsSystem.entity.TeacherInfo;

import java.util.ArrayList;
import java.util.HashMap;

public interface TeacherInfoService{
    boolean insertTeacherInfo(TeacherInfo teacherInfo);
    boolean insertTeacherInfo(HashMap m);
    boolean deleteTeacherInfo(TeacherInfo teacherInfo);
    boolean deleteTeacherInfo(HashMap m);
    boolean updateTeacherInfo(TeacherInfo teacherInfo);
    boolean updateTeacherInfo(HashMap m);

    ArrayList<TeacherInfo> getTeacherInfos(HashMap m);
}
