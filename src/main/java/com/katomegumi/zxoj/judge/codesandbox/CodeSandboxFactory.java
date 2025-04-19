package com.katomegumi.zxoj.judge.codesandbox;

import com.katomegumi.zxoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * @author : 惠
 * @description : 代码沙箱工厂 根据对应字符串生成对应的沙箱
 * @createDate : 2025/4/18 下午5:35
 */
public class CodeSandboxFactory {

    /**
     * 根据字符串生成对应的沙箱
     * @param type
     * @return
     */
    public static CodeSandbox newInstance(String type){
        switch (type) {
            case "example":
               return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}

