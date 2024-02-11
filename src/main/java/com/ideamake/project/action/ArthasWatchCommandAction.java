package com.ideamake.project.action;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.ideamake.project.common.command.CommandContext;
import com.ideamake.project.common.enums.ShellScriptCommandEnum;
import com.ideamake.project.ui.ArthasOptionsDialog;

/**
 * watch展开结构
 *
 * @author 汪小哥
 * @date 21-12-2019
 */
public class ArthasWatchCommandAction extends BaseArthasPluginAction {

    public ArthasWatchCommandAction() {
        this.setSupportEnum(true);
    }

    @Override
    public void doCommand(String className, String methodName, Project project, PsiElement psiElement) {
        CommandContext commandContext = new CommandContext(project, psiElement);
        ShellScriptCommandEnum scriptCommandEnum = ShellScriptCommandEnum.WATCH;
        //这里针对放置在字段上获取字段的值的信息进行处理增强
        if (ShellScriptCommandEnum.WATCH_STATIC_FILED.support(commandContext)) {
            scriptCommandEnum = ShellScriptCommandEnum.WATCH_STATIC_FILED;
        } else if (ShellScriptCommandEnum.WATCH_NON_STATIC_FILED.support(commandContext)) {
            // 当获取某个非静态的字段的时候，只能是调用构造、或者非静态方法才可以，这里增加一个表达式判断逻辑
            // 默认为 1== 1
            // 如果调用静态方法 获取 target.xxxField 这样会报错的~ 这里要添加一个条件限制一下
            // 具体可以参考 src/main/java/com/taobao/arthas/core/advisor/ArthasMethod.java
            scriptCommandEnum = ShellScriptCommandEnum.WATCH_NON_STATIC_FILED;
        }
        String command = scriptCommandEnum.getArthasCommand(commandContext);

        new ArthasOptionsDialog(project, command).open();
    }
}