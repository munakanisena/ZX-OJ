package com.katomegumi.zxoj.judge.codesandbox.impl;

import com.katomegumi.zxoj.judge.codesandbox.CodeSandbox;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author : 惠
 * @description :
 * @createDate : 2025/4/18 下午5:32
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程沙箱");
        return null;
    }
}

