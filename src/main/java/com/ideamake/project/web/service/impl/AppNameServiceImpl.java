package com.ideamake.project.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.ideamake.project.utils.Env;
import com.ideamake.project.web.HttpUtil;
import com.ideamake.project.web.service.AppNameService;

import java.util.List;
import java.util.Optional;

/**
 * @author: wuziqi
 * @description: 应用列表服务实现类
 * @date: 2023/8/9
 */
public class AppNameServiceImpl implements AppNameService {
    private static final String testUrl = "https://test-tunnel-server.ideamake.cn/api/tunnelApps";

    private static final String prodUrl = "https://tunnel-server.ideamake.cn/api/tunnelApps";

    @Override
    public List<String> getAppNamesForTest() {
        String res = HttpUtil.get(testUrl);
        return Optional.ofNullable(res).map(str -> JSON.parseArray(str, String.class)).map(list -> {
            list.sort(String::compareTo);
            return list;
        }).orElseThrow(() -> new RuntimeException("网络异常，请检查网络"));
    }

    @Override
    public List<String> getAppNamesForProd() {
        String res = HttpUtil.get(prodUrl);
        return Optional.ofNullable(res).map(str -> JSON.parseArray(str, String.class)).map(list -> {
            list.sort(String::compareTo);
            return list;
        }).orElseThrow(() -> new RuntimeException("网络异常，请检查网络"));
    }

    @Override
    public List<String> getAppNames(Env env) {
        return Env.TEST.equals(env) ? getAppNamesForTest() : getAppNamesForProd();
    }
}
