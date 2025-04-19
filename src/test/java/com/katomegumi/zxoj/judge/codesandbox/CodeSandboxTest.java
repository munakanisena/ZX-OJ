package com.katomegumi.zxoj.judge.codesandbox;

import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.katomegumi.zxoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

class CodeSandboxTest {

    @Value("${codeSandBox.type:example}")
    private String type;

    @Test
    void contextLoads() {

        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    @Test
    void contextLoadsByProxy() {

        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance("example");
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandBox);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}