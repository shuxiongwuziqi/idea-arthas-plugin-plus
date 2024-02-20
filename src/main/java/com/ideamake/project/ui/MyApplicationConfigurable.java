package com.ideamake.project.ui;


import com.ideamake.project.setting.ApplicationSettingsState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wuziqi
 * @date 2024/02/20
 * @description 描述信息
 */
public class MyApplicationConfigurable implements Configurable {

    private ApplicationSettingsState service;
    private MyConfig myConfig;


    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "arthas-plus";
    }

    public MyApplicationConfigurable() {
       this.service = ServiceManager.getService(ApplicationSettingsState.class);
    }

    @Override
    public @Nullable JComponent createComponent() {
        MyConfig myConfig = new MyConfig();
        this.myConfig = myConfig;
        if (Objects.nonNull(this.service.foregroundColor)) {
            myConfig.getForegroundColor().setSelectedColor(new Color(this.service.foregroundColor));
        }
        if (Objects.nonNull(this.service.enableForegroundColor)) {
            myConfig.getForegroundCheckBox().setSelected(this.service.enableForegroundColor);
        }
        if (Objects.nonNull(this.service.backgroundColor)) {
            myConfig.getBackgroundColor().setSelectedColor(new Color(this.service.backgroundColor));
        }
        if (Objects.nonNull(this.service.enableBackgroundColor)) {
            myConfig.getBackgroundCheckBox().setSelected(this.service.enableBackgroundColor);
        }
        if (Objects.nonNull(this.service.effectColor)) {
            myConfig.getEffectColor().setSelectedColor(new Color(this.service.effectColor));
        }
        if (Objects.nonNull(this.service.effectStyle)) {
            myConfig.getEffectStyle().setSelectedItem(this.service.effectStyle);
        }
        if (Objects.nonNull(this.service.enableEffectColor)) {
            myConfig.getEffectCheckBox().setSelected(this.service.enableEffectColor);
        }
        return myConfig.getPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        this.service.enableForegroundColor = this.myConfig.getForegroundCheckBox().isSelected();
        this.service.enableBackgroundColor = this.myConfig.getBackgroundCheckBox().isSelected();
        this.service.enableEffectColor = this.myConfig.getEffectCheckBox().isSelected();
        this.service.foregroundColor = Optional.ofNullable(this.myConfig.getForegroundColor().getSelectedColor()).map(Color::getRGB).orElse(null);
        this.service.backgroundColor = Optional.ofNullable(this.myConfig.getBackgroundColor().getSelectedColor()).map(Color::getRGB).orElse(null);
        this.service.effectColor = Optional.ofNullable(this.myConfig.getEffectColor().getSelectedColor()).map(Color::getRGB).orElse(null);
        this.service.effectStyle = this.myConfig.getEffectStyle().getSelectedItem().toString();
    }
}