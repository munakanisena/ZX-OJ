package com.katomegumi.zxoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.katomegumi.zxoj.model.dto.question.JudgeCase;
import com.katomegumi.zxoj.model.dto.question.JudgeConfig;
import com.katomegumi.zxoj.model.dto.questionsubmit.JudgeInfo;
import com.katomegumi.zxoj.model.entity.Question;
import com.katomegumi.zxoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @author : 惠
 * @description :  默认策略
 * @createDate : 2025/4/19 上午11:49
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        //构造判提后的信息
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;

        //输出于预期输出 数量
        if (outputList.size()!=inputList.size()){
            judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCaseList.size(); i++) {
            if (!judgeCaseList.get(i).getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = judgeConfig.getTimeLimit();
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        if (memory>needMemoryLimit){
            judgeInfoMessageEnum=JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time>needTimeLimit){
            judgeInfoMessageEnum=JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        return judgeInfoResponse;
    }
}



