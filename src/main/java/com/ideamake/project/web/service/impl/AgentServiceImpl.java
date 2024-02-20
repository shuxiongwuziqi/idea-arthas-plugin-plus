package com.ideamake.project.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ideamake.project.setting.ApplicationSettingsState;
import com.ideamake.project.web.entity.AgentInfo;
import com.ideamake.project.web.service.AgentService;
import com.ideamake.project.utils.Env;
import com.ideamake.project.web.HttpUtil;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * @author: wuziqi
 * @description: agent服务实现类
 * @date: 2023/8/9
 */
public class AgentServiceImpl implements AgentService {

    @Override
    public Map<String, AgentInfo> getAgentInfoArtifactId(String appName, Env env) {
        String res =HttpUtil.get(env.getAgentInfoUrl(appName));
        return getAgentInfoMap(res);
    }

    /**
     * 把agentId填充到AgentInfo中，方便后续处理
     *
     * @param res 返回
     * @return AgentInfoMap
     */
    @Nullable
    private static Map<String, AgentInfo> getAgentInfoMap(String res) {
        return Optional.ofNullable(res).map(str -> JSON.parseObject(str, new TypeReference<Map<String, AgentInfo>>() {
        })).map(infoMap -> {
            infoMap.keySet().forEach(id -> infoMap.get(id).setAgentId(id));
            return infoMap;
        }).orElse(null);
    }
}
