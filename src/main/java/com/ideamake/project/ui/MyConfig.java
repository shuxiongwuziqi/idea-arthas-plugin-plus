package com.ideamake.project.ui;


import com.intellij.ui.ColorPanel;
import lombok.Data;

import javax.swing.*;

/**
 * @author wuziqi
 * @date 2024/02/20
 * @description 描述信息
 */
@Data
public class MyConfig {
    private JPanel contentPane;
    private JTabbedPane settingTabPane;
    private JCheckBox foregroundCheckBox;
    private JComboBox effectStyle;
    private JCheckBox backgroundCheckBox;
    private JCheckBox effectCheckBox;
    private ColorPanel foregroundColor;
    private ColorPanel backgroundColor;
    private ColorPanel effectColor;

    public MyConfig() {
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}