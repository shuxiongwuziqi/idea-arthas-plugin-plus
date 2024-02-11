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
     * 获取测试环境应用列表
     * @return 测试环境应用列表
     */
    List<String> getAppNamesForTest();

    /**
     * 获取正式环境应用列表
     * @return 正式环境应用列表
     */
    List<String> getAppNamesForProd();

    /**
     * 根据环境获取应用列表
     * @param env 环境
     * @return 应用列表
     */
    List<String> getAppNames(Env env);
}
