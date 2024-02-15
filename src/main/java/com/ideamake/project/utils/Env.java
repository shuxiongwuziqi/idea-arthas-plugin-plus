package com.ideamake.project.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author imyzt
 * @date 2023/08/11
 * @description 描述信息
 */

@Getter
@ToString
@AllArgsConstructor
public enum Env {

    TEST("https://test-tunnel-server.ideamake.cn/api/tunnelAgentInfo?app=%s", "wss://test-tunnel-server.ideamake.cn:7777/ws?method=connectArthas&id=%s&targetServer=%s"),
    PRE("https://tunnel-server.ideamake.cn/api/tunnelAgentInfo?app=%s", "wss://tunnel-server.ideamake.cn:7777/ws?method=connectArthas&id=%s&targetServer=%s"),
    PROD("https://tunnel-server.ideamake.cn/api/tunnelAgentInfo?app=%s", "wss://tunnel-server.ideamake.cn:7777/ws?method=connectArthas&id=%s&targetServer=%s"),

    ;

    private final String getAgentInfoUrl;
    private final String getWsUrl;

    public String getWsUrl(String agentId, String host) {
        return String.format(this.getWsUrl, agentId, host);
    }

    public static String getEnvNameByGitBranch(String gitBranch) {
        if (gitBranch == null) {
            return null;
        }
        gitBranch = gitBranch.toUpperCase();

        for (Env env : Env.values()) {
            if (gitBranch.contains(env.name())) {
                return env.name();
            }
        }

        return null;
    }
}
