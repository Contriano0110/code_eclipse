package com.gdtsSystem.service.Interface;


import com.gdtsSystem.entity.GdtInfo;

import java.util.ArrayList;
import java.util.HashMap;

public interface GdtInfoService {
    ArrayList<GdtInfo> getGdtInfos(String find);
    ArrayList<GdtInfo> getGdtInfos(HashMap m);
    ArrayList<GdtInfo> getGdtInfos_ed(HashMap m);
    //ArrayList<GdtInfo> getGdtInfos_teacher(int tid, String find);
    ArrayList<GdtInfo> getGdtInfos_student(String sid, String find);
    boolean insertGdtInfo(GdtInfo gdtInfo);
    boolean insertGdtInfo(HashMap m);
    boolean deleteGdtInfo(GdtInfo gdtInfo);
    boolean deleteGdtInfo(HashMap m);
    boolean updateGdtInfo(GdtInfo gdtInfo);
    boolean updateGdtInfo(HashMap m);
    HashMap getGdtInfos_2(HashMap m);

    boolean applyGdtInfo(HashMap m);
}