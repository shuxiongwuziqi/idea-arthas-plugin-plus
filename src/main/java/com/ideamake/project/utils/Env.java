package com.ideamake.project.utils;

import com.ideamake.project.setting.ApplicationSettingsState;
import com.ideamake.project.web.HttpUtil;
import com.intellij.openapi.components.ServiceManager;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author imyzt
 * @date 2023/08/11
 * @description 描述信息
 */
@ToString
@AllArgsConstructor
public enum Env {

    TEST("https://%s/api/tunnelApps", "https://%s/api/tunnelAgentInfo?app=%s", "wss://%s:7777/ws?method=connectArthas&id=%s&targetServer=%s"),
    PRE("https://%s/api/tunnelApps", "https://%s/api/tunnelAgentInfo?app=%s", "wss://%s:7777/ws?method=connectArthas&id=%s&targetServer=%s"),
    PROD("https://%s/api/tunnelApps", "https://%s/api/tunnelAgentInfo?app=%s", "wss://%s:7777/ws?method=connectArthas&id=%s&targetServer=%s"),

    ;

    private final String appListUrl;
    private final String agentInfoUrl;
    private final String wsUrl;

    public String getWsUrl(String agentId, String host) {
        String domain = getDomain(this);
        return String.format(this.wsUrl, domain, agentId, host);
    }

    @Nullable
    private static String getDomain(Env that) {
        String domain = null;
        ApplicationSettingsState service = ServiceManager.getService(ApplicationSettingsState.class);
        switch (that) {
            case TEST:
                domain = Optional.ofNullable(service.testDomain).orElse("test-tunnel-server.ideamake.cn");
                break;
            case PRE:
                domain = Optional.ofNullable(service.preDomain).orElse("tunnel-server.ideamake.cn");
                break;
            case PROD:
                domain = Optional.ofNullable(service.prodDomain).orElse("tunnel-server.ideamake.cn");
                break;
        }
        return domain;
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

    public String getAppListUrl() {
        ApplicationSettingsState service = ServiceManager.getService(ApplicationSettingsState.class);
        if (this.name().equals("TEST")) {
            return String.format(this.appListUrl, StringUtils.isBlank(service.testDomain) ? "test-tunnel-server.ideamake.cn" : service.testDomain);
        }
        if (this.name().equals("PRE")) {
            return String.format(this.appListUrl, StringUtils.isBlank(service.preDomain) ? "tunnel-server.ideamake.cn" : service.preDomain);
        }
        if (this.name().equals("PROD")) {
            return String.format(this.appListUrl, StringUtils.isBlank(service.prodDomain) ? "tunnel-server.ideamake.cn" : service.prodDomain);
        }
        return appListUrl;
    }

    public String getAgentInfoUrl(String appName) {
        String domain = getDomain(this);
        return String.format(this.agentInfoUrl, domain, appName);
    }
}
