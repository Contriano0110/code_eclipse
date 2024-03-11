package com.gdtsSystem.service.Interface;

import com.gdtsSystem.entity.ApplyInfo;

import java.util.ArrayList;
import java.util.HashMap;

public interface ApplyInfoService {
    ArrayList<ApplyInfo> getApplyInfos(String find);

    boolean insertApplyInfo(ApplyInfo applyInfo);
    boolean insertApplyInfo(HashMap m);
    boolean deleteApplyInfo(ApplyInfo applyInfo);
    boolean deleteApplyInfo(HashMap m);
    boolean updateApplyInfo(ApplyInfo applyInfo);
    boolean updateApplyInfo(HashMap m);

    HashMap getApplyInfos_2(HashMap m);
    HashMap getApplyInfos(HashMap m);
    HashMap getApplyInfos_t(HashMap m);
    HashMap getApplyInfos_tt(HashMap m);

    boolean updateApplyInfo_status(HashMap m);
}
