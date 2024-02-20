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

    @Override
    public List<String> getAppNames(Env env) {
        String res = HttpUtil.get(env.getAppListUrl());
        return Optional.ofNullable(res).map(str -> JSON.parseArray(str, String.class)).map(list -> {
            list.sort(String::compareTo);
            return list;
        }).orElseThrow(() -> new RuntimeException("网络异常，请检查网络"));
    }
}
