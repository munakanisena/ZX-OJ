package com.katomegumi.zxoj.judge;

import cn.hutool.json.JSONUtil;
import com.katomegumi.zxoj.common.ErrorCode;
import com.katomegumi.zxoj.exception.BusinessException;
import com.katomegumi.zxoj.judge.codesandbox.CodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.CodeSandboxFactory;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.katomegumi.zxoj.judge.strategy.JudgeContext;
import com.katomegumi.zxoj.model.dto.question.JudgeCase;
import com.katomegumi.zxoj.judge.codesandbox.model.JudgeInfo;
import com.katomegumi.zxoj.model.entity.Question;
import com.katomegumi.zxoj.model.entity.QuestionSubmit;
import com.katomegumi.zxoj.model.enums.QuestionSubmitStatusEnum;
import com.katomegumi.zxoj.service.QuestionService;
import com.katomegumi.zxoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : 惠
 * @description : 判题 服务
 * @createDate : 2025/4/18 下午7:34
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codeSandBox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        if (questionSubmitId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目Id为空");
        }
        //1 判断 题目是否存在 题目任务是否存在
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目任务不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //2 判断 题目任务状态是否 是等待中
        if (!Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目任务不是等待状态");
        }
        //3 设置成 判题中 并且进行校验
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean result = questionSubmitService.updateById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"设置失败,请重试");
        }

        Long id = questionSubmit.getId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        //转成对象 获得input
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        //4 执行 沙箱
        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        //5 根据沙箱的结果 判断题目是否成功
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setOutputList(outputList);
        judgeContext.setInputList(inputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        //进行判题结果 根据语言 使用不同的判题逻辑
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        //6修改 判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        result = questionSubmitService.updateById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"设置失败,请重试");
        }
        //返回任务
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}

