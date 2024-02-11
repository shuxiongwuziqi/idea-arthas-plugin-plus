package com.ideamake.project.web.service;

import com.ideamake.project.web.entity.AgentInfo;
import com.ideamake.project.utils.Env;

import java.util.Map;

/**
 * @author: wuziqi
 * @description: agent服务
 * @date: 2023/8/9
 */
public interface AgentService {
    Map<String, AgentInfo> getAgentInfoForTest(String appName);

    Map<String, AgentInfo> getAgentInfoForProd(String appName);

    Map<String, AgentInfo> getAgentInfoArtifactId(String appName, Env env);
}
