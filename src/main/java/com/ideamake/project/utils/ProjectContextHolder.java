package com.ideamake.project.utils;


import com.ideamake.project.web.entity.AgentInfo;
import com.ideamake.project.web.service.AgentService;
import com.ideamake.project.web.service.AppNameService;
import com.ideamake.project.web.service.impl.AgentServiceImpl;
import com.ideamake.project.web.service.impl.AppNameServiceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author imyzt
 * @date 2023/08/11
 * @description 描述信息
 */
public class ProjectContextHolder {

    private static final AgentService agentService = new AgentServiceImpl();

    /**
     * 根据项目，环境和模块
     *
     * @param env        环境
     * @param artifactId artifactId
     * @param moduleName 模块名字
     * @return agent信息列表
     */
    public static List<AgentInfo> getAgentList(Env env, String artifactId, String moduleName) {
        AppNameService appNameService = new AppNameServiceImpl();
        List<String> appNames = appNameService.getAppNames(env);

        // 先根据artifactId进行匹配，如果匹配不到，再使用name匹配
        Optional<String> matchedAppName = appNames.stream().filter(name -> name.contains(artifactId) && name.contains(env.name().toLowerCase())).min(Comparator.comparingInt(String::length));
        if (!matchedAppName.isPresent() && StringUtils.isNoneBlank(moduleName)) {
            matchedAppName = appNames.stream().filter(name -> name.contains(moduleName) && name.contains(env.name().toLowerCase())).min(Comparator.comparingInt(String::length));
        }
        return matchedAppName.map(appName -> new ArrayList<>(agentService.getAgentInfoArtifactId(appName, env).values())).orElse(null);
    }
}
