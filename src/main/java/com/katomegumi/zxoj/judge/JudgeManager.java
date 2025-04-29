package com.katomegumi.zxoj.judge;

import com.katomegumi.zxoj.judge.strategy.DefaultJudgeStrategy;
import com.katomegumi.zxoj.judge.strategy.JavaJudgeStrategy;
import com.katomegumi.zxoj.judge.strategy.JudgeContext;
import com.katomegumi.zxoj.judge.strategy.JudgeStrategy;
import com.katomegumi.zxoj.judge.codesandbox.model.JudgeInfo;
import org.springframework.stereotype.Service;

/**
 * @author : 惠
 * @description : 进行封装判别 使用哪个判题策略(简化调用)
 * @createDate : 2025/4/19 下午12:00
 */
@Service
public class JudgeManager {
    /**
     * 执行判题(结果)
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        String language = judgeContext.getQuestionSubmit().getLanguage();
        if ("java".equals(language)) {
         judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}

