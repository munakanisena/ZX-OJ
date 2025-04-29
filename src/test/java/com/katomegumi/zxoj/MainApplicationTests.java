package com.katomegumi.zxoj;

import com.katomegumi.zxoj.judge.codesandbox.CodeSandboxFactory;
import com.katomegumi.zxoj.judge.codesandbox.CodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.katomegumi.zxoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/likatomegumi">程序员鱼皮</a>
 * @from <a href="https://katomegumi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Value("${codeSandBox.type:example}")
    private String type;

    @Test
    void contextLoads() {

        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果:\" + (a + b));\n" +
                "    }\n" +
                "}\n";
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

}
