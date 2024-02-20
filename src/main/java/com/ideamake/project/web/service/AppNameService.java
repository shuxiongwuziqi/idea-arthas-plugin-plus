package com.ideamake.project.web.service;

import com.ideamake.project.utils.Env;

import java.util.List;

/**
 * @author: wuziqi
 * @description: 应用列表服务
 * @date: 2023/8/9
 */
public interface AppNameService {

    /**
     * 根据环境获取应用列表
     * @param env 环境
     * @return 应用列表
     */
    List<String> getAppNames(Env env);
}
