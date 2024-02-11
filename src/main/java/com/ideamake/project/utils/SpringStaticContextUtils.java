package com.ideamake.project.utils;

import com.google.common.base.Splitter;
import com.ideamake.project.constants.ArthasCommandConstants;
import com.ideamake.project.setting.AppSettingsState;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 * spring 静态的context 工具类
 *
 * @author 汪小哥
 * @date 13-08-2020
 */
public class SpringStaticContextUtils {

    public static String getStaticSpringContextClassName(Project project) {
        String springStaticContextConfig = getSpringStaticContextConfig(project);
        //#springContext=填充,#springContext.getBean("%s")
        // 获取class的classloader
        List<String> springContextCLassLists = Splitter.on('@').omitEmptyStrings().splitToList(springStaticContextConfig);
        if (springContextCLassLists.isEmpty()) {
            throw new IllegalArgumentException("Static Spring context requires manual configuration");
        }
        //@com.wj.study.demo.generator.ApplicationContextProvider@context  配置的是这个玩意
        return springContextCLassLists.get(0);
    }

    /**
     * 是否配置 static spring context 上下文
     *
     * @param project
     * @return
     */
    public static boolean booleanConfigStaticSpringContext(Project project) {
        try {
            getStaticSpringContextClassName(project);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取spring static context的配置
     */
    private static String getSpringStaticContextConfig(Project project) {
        // 这里换个获取配置的方式
        AppSettingsState instance = AppSettingsState.getInstance(project);
        String springContextValue = instance.staticSpringContextOgnl;
        if (StringUtils.isBlank(springContextValue) || ArthasCommandConstants.DEFAULT_SPRING_CONTEXT_SETTING.equals(springContextValue)) {
            throw new IllegalArgumentException("Static Spring context requires manual configuration");
        }
        if (springContextValue.endsWith(",")) {
            springContextValue = springContextValue.substring(0, springContextValue.length() - 2);
        }
        return springContextValue;
    }
}
