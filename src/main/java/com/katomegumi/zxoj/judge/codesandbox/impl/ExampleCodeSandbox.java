package com.katomegumi.zxoj.judge.codesandbox.impl;

import com.katomegumi.zxoj.judge.codesandbox.CodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.katomegumi.zxoj.model.dto.questionsubmit.JudgeInfo;
import com.katomegumi.zxoj.model.enums.JudgeInfoMessageEnum;
import com.katomegumi.zxoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @author : 惠
 * @description :
 * @createDate : 2025/4/18 下午5:32
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}

