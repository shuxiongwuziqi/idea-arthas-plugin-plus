package com.ideamake.project.action;


import com.ideamake.project.common.command.CommandContext;
import com.ideamake.project.common.enums.ShellScriptCommandEnum;
import com.ideamake.project.ui.ArthasOptionsDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author wuziqi
 * @date 2024/02/20
 * @description 描述信息
 */
public class SqlCommandAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        DataContext dataContext = event.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        String command = "watch com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor prepareStatement '{returnObj.delegate.toString().replace(\"\\n\",\" \")}' -n 100  -x 3 ";
        new ArthasOptionsDialog(project, command, editor).open();
    }
}