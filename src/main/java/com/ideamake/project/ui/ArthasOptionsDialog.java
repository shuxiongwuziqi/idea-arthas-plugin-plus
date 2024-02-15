package com.ideamake.project.ui;

import com.ideamake.project.gui.MyBatisLogManager;
import com.ideamake.project.setting.AppSettingsState;
import com.ideamake.project.utils.Env;
import com.ideamake.project.utils.NotifyUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * options
 *
 * @author 汪小哥
 * @date 01-01-2021
 */
public class ArthasOptionsDialog extends JDialog {
    public static final String TEST = "TEST";
    public static final String MODULE_NOT_EXIST = "这个项目没有模块";
    private JPanel contentPane;
    private JComboBox<String> envSelect;
    private JTextArea commendEdit;
    private JButton execBtn;
    private JComboBox<String> artifactSelect;

    private final Project project;

    private AppSettingsState setting;

    public ArthasOptionsDialog(Project project, String command, Editor editor) {
        this.project = project;
        setContentPane(this.contentPane);
        setModal(false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        init(project, command, editor);

    }

    private void init(Project project, String command, Editor editor) {
        // 填充模块选择器
        List<Model> moduleList = new ArrayList<>(4);
        String currentArtifactId = getModuleList(project, moduleList);
        if (CollectionUtils.isEmpty(moduleList)) {
            NotifyUtils.notifyMessage(project, MODULE_NOT_EXIST);
            dispose();
            return;
        }
        moduleList.forEach((module) -> artifactSelect.addItem(module.getArtifactId()));

        for (Env value : Env.values()) {
            envSelect.addItem(value.name());
        }
        commendEdit.setText(command);

        // 设置环境和模块选择器
        setting = AppSettingsState.getInstance(project);

        String gitBranch = null;
        try {
            Git git = Git.open(new File(project.getBasePath(), ".git"));
            gitBranch = git.getRepository().getBranch();
        } catch (IOException e) {
            // 获取git失败，不做处理
        }
        envSelect.setSelectedItem(Optional.ofNullable(Env.getEnvNameByGitBranch(gitBranch)).orElse(Optional.ofNullable(setting.lastSelectEnv).orElse(TEST)));
        envSelect.addActionListener((e) -> setting.lastSelectEnv = (String) envSelect.getSelectedItem());

        artifactSelect.setSelectedItem(Optional.ofNullable(currentArtifactId).orElse(moduleList.get(0).getArtifactId()));

        execBtn.addActionListener((e) -> {
            String newCommend = commendEdit.getText();
            Env env = Env.valueOf((String) envSelect.getSelectedItem());
            String selectedArtifactId = (String) artifactSelect.getSelectedItem();
            String selectedModuleName = moduleList.stream().filter(module -> module.getArtifactId().equals(selectedArtifactId)).findFirst()
                    .map(Model::getName).orElse(null);
            MyBatisLogManager.run(project, newCommend, env, selectedArtifactId, selectedModuleName, editor);
            onCancel();
        });
    }

    /**
     * 扫描project下面的模块名字
     *
     * @param project 项目
     * @return 当前模块artifactId
     */
    private static String getModuleList(Project project, List<Model> moduleList) {
        // 获取当前文件所属模块
        Module currentFileModule = getCurrentFileModule(project);

        String currentArtifactId = null;
        // 填充模块选择器
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            String moduleFilePath = module.getModuleFilePath().replace("/.idea", "");
            moduleFilePath = moduleFilePath.substring(0, moduleFilePath.lastIndexOf("/"));
            String pomFile = moduleFilePath + "/pom.xml";
            try {
                Model model = new MavenXpp3Reader().read(Files.newInputStream(new File(pomFile).toPath()));
                moduleList.add(model);
                if (currentFileModule != null && currentFileModule.equals(module)) {
                    currentArtifactId = model.getArtifactId();
                }
            } catch (IOException | XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }

        return currentArtifactId;
    }

    @Nullable
    private static Module getCurrentFileModule(Project project) {
        Module currentFileModule = null;
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            VirtualFile currentFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (currentFile != null) {
                currentFileModule = ModuleUtilCore.findModuleForFile(currentFile, project);
            }
        }
        return currentFileModule;
    }

    /**
     * 关闭
     */
    private void onCancel() {
        dispose();
    }

    /**
     * 打开窗口
     */
    public void open() {
        setTitle("arthas options use");
        pack();
        //两个屏幕处理出现问题，跳到主屏幕去了 https://blog.csdn.net/weixin_33919941/article/details/88129513
        setLocationRelativeTo(WindowManager.getInstance().getFrame(this.project));
        setVisible(true);
    }
}
