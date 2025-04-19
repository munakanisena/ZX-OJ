package com.katomegumi.zxoj.judge.codesandbox;

import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.katomegumi.zxoj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
