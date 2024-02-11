package com.ideamake.project.setting;

import com.ideamake.project.constants.ArthasCommandConstants;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Supports storing the application settings in a persistent way.
 * The State and Storage annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 *
 * @author 汪小哥
 * @date 14-08-2020
 */
@State(
        name = "arthas.idea.plugin",
        storages = {@Storage("setting.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {


    /**
     * 上次选择的环境
     */
    public String lastSelectEnv;

    /**
     * spring ognl 配置
     */
    public String staticSpringContextOgnl = ArthasCommandConstants.DEFAULT_SPRING_CONTEXT_SETTING;

    /**
     * 跳过jdk trace
     */
    public boolean traceSkipJdk = false;

    /**
     * 调用次数
     */
    public String invokeMonitorCount = ArthasCommandConstants.INVOKE_MONITOR_COUNT;
    /**
     * 时间间隔
     */
    public String invokeMonitorInterval = ArthasCommandConstants.INVOKE_MONITOR_INTERVAL;

    /**
     * 调用次数
     */
    public String invokeCount = ArthasCommandConstants.INVOKE_COUNT;

    /**
     * 打印属性的深度
     */
    public String depthPrintProperty = ArthasCommandConstants.RESULT_X;

    /**
     * 是否展示默认的条件表达式 [修改 部分低版本不支持]
     */
    public boolean conditionExpressDisplay = false;

    /**
     * watch/trace/monitor support verbose option, print ConditionExpress result #1348
     */
    public boolean printConditionExpress = false;

    /**
     * 获取工程的名称
     *
     * @return
     */
    public static Project getProject() {
        return projectInfo;
    }

    private static Project projectInfo;


    public static AppSettingsState getInstance(@NotNull Project project) {
        projectInfo = project;

        return ServiceManager.getService(project, AppSettingsState.class);
    }



    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
