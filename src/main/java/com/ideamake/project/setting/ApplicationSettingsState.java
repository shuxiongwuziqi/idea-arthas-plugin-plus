package com.ideamake.project.setting;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wuziqi
 * @date 2024/02/20
 * @description 描述信息
 */
@State(
        name = "arthas.idea.plugin.global",
        storages = {@Storage("arthas-plus-setting.xml")}
)
public class ApplicationSettingsState implements PersistentStateComponent<ApplicationSettingsState> {
    @Override
    public @Nullable ApplicationSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApplicationSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }


    public Boolean enableForegroundColor;
    public Integer foregroundColor;
    public Boolean enableBackgroundColor;
    public Integer backgroundColor;
    public Boolean enableEffectColor;
    public Integer effectColor;
    public String effectStyle;
    public String testDomain;
    public String preDomain;
    public String prodDomain;
}