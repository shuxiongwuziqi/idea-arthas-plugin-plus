package com.ideamake.project.action;

import com.ideamake.project.common.command.CommandContext;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.ideamake.project.common.enums.ShellScriptCommandEnum;
import com.ideamake.project.ui.ArthasOptionsDialog;

/**
 * 输出当前方法被调用的调用路径
 * <p>
 * 很多时候我们都知道一个方法被执行，但这个方法被执行的路径非常多，或者你根本就不知道这个方法是从那里被执行了，此时你需要的是 stack 命令。
 *
 * @author 汪小哥
 * @date 09-01-2020
 */
public class ArthasStackCommandAction extends BaseArthasPluginAction {

    public ArthasStackCommandAction() {
        this.setSupportEnum(true);
    }

    @Override
    public void doCommand(String className, String methodName, Project project, PsiElement psiElement, Editor editor) {
        CommandContext commandContext = new CommandContext(project, psiElement);
        String command = ShellScriptCommandEnum.STACK.getArthasCommand(commandContext);
        new ArthasOptionsDialog(project, command, editor).open();
    }

}
