package com.katomegumi.zxoj.judge;

import com.katomegumi.zxoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @author : 惠
 * @description : 判题服务
 * @createDate : 2025/4/18 下午7:33
 */
public interface JudgeService {
    /**
     * 执行判题服务
     * @param questionSubmitId
     * @return 返回 判题任务
     */
    QuestionSubmit doJudge(Long questionSubmitId);
}

