package com.katomegumi.zxoj.judge.strategy;

import com.katomegumi.zxoj.model.dto.questionsubmit.JudgeInfo;


public interface JudgeStrategy {
    /**
     * 执行判题(结果)
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
