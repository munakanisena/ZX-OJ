package com.katomegumi.zxoj.judge.strategy;

import com.katomegumi.zxoj.model.dto.question.JudgeCase;
import com.katomegumi.zxoj.model.dto.question.JudgeConfig;
import com.katomegumi.zxoj.model.dto.questionsubmit.JudgeInfo;
import com.katomegumi.zxoj.model.entity.Question;
import com.katomegumi.zxoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @author : 惠
 * @description : 上下文类
 * @createDate : 2025/4/19 上午11:43
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> outputList;

    private List<String> inputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}

