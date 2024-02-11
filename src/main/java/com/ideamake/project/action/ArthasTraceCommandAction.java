package com.ideamake.project.action;

import com.ideamake.project.common.command.CommandContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.ideamake.project.common.enums.ShellScriptCommandEnum;
import com.ideamake.project.ui.ArthasOptionsDialog;

/**
 * trace 命令  https://arthas.aliyun.com/doc/trace.html 默认打开 不跳过JDK的方法
 *
 * @author 汪小哥
 * @date 21-12-2019
 */
public class ArthasTraceCommandAction extends BaseArthasPluginAction {

    public ArthasTraceCommandAction() {
        this.setSupportEnum(true);
    }

    @Override
    public void doCommand(String className, String methodName, Project project, PsiElement psiElement) {
        CommandContext commandContext = new CommandContext(project, psiElement);
        String command = ShellScriptCommandEnum.TRACE.getArthasCommand(commandContext);
        new ArthasOptionsDialog(project, command).open();

    }
}
