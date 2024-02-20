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
    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JCheckBox foregroundCheckBox;
    private ColorPanel foregroundColor;
    private JCheckBox backgroundCheckBox;
    private JCheckBox effectCheckBox;
    private ColorPanel backgroundColor;
    private ColorPanel effectColor;
    private JComboBox effectStyle;
}