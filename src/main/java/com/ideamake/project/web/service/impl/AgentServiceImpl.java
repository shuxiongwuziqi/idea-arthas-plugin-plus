package com.ideamake.project.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ideamake.project.web.entity.AgentInfo;
import com.ideamake.project.web.service.AgentService;
import com.ideamake.project.utils.Env;
import com.ideamake.project.web.HttpUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * @author: wuziqi
 * @description: agent服务实现类
 * @date: 2023/8/9
 */
public class AgentServiceImpl implements AgentService {

    private static final String testUrl = "https://test-tunnel-server.ideamake.cn/api/tunnelAgentInfo?app=%s";

    private static final String prodUrl = "https://tunnel-server.ideamake.cn/api/tunnelAgentInfo?app=%s";

    @Override
    public Map<String, AgentInfo> getAgentInfoForTest(String appName) {
        String res = HttpUtil.get(String.format(testUrl, appName));
        return getAgentInfoMap(res);
    }

    @Override
    public Map<String, AgentInfo> getAgentInfoForProd(String appName) {
        String res = HttpUtil.get(String.format(prodUrl, appName));
        return getAgentInfoMap(res);
    }

    @Override
    public Map<String, AgentInfo> getAgentInfoArtifactId(String appName, Env env) {
        return Env.TEST.equals(env) ? getAgentInfoForTest(appName) : getAgentInfoForProd(appName);
    }

    /**
     * 把agentId填充到AgentInfo中，方便后续处理
     * @param res 返回
     * @return AgentInfoMap
     */
    @Nullable
    private static Map<String, AgentInfo> getAgentInfoMap(String res) {
        return Optional.ofNullable(res).map(str -> JSON.parseObject(str, new TypeReference<Map<String, AgentInfo>>() { })).map(infoMap -> {
            infoMap.keySet().forEach(id -> infoMap.get(id).setAgentId(id));
            return infoMap;
        }).orElse(null);
    }
}
