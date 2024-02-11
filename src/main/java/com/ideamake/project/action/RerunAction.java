package com.ideamake.project.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.ideamake.project.gui.MyBatisLogManager;
import org.jetbrains.annotations.NotNull;

/**
 * RerunAction
 * @author huangxingguang
 */
public class RerunAction extends AnAction {

    private final MyBatisLogManager manager;

    public RerunAction(MyBatisLogManager manager) {
        super("Rerun", "Rerun", AllIcons.Actions.Restart);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        manager.rerun();
    }

}