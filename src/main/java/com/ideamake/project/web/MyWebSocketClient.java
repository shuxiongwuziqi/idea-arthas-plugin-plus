package com.ideamake.project.web;

import com.ideamake.project.setting.ApplicationSettingsState;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * websocket客户端监听类
 *
 * @author 。
 */
public class MyWebSocketClient extends WebSocketClient implements Disposable {

    public static final String WEBSOCKET_CLOSE = " websocket close";
    public static final String WEBSOCKET_OPEN = " websocket open";
    public static final String WEBSOCKET_ERROR = "{} websocket error {}";
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);
    public static final String LISTENING_STR = " listening...\n";
    public static final String ENTER = "\n";

    private ConsoleView consoleView;

    public void setConsoleView(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    private final String agentId;

    private final String msg;

    private final static String START_WORD = "Affect";

    private boolean isStartPrint;

    private MarkupModel markupModel;

    private List<RangeHighlighter> highlighters;


    public MyWebSocketClient(URI serverUri, String agentId, String msg, Editor editor) {
        super(serverUri);
        this.agentId = agentId;
        this.msg = msg;
        this.isStartPrint = false;
        this.markupModel = editor.getMarkupModel();
        this.highlighters = new ArrayList<>();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info(agentId + WEBSOCKET_OPEN);
        // 延迟发送，确保对方可以收到这条命令
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        send(msg);
    }

    @Override
    public void onMessage(String s) {
        if (Objects.nonNull(s)) {
            if (isStartPrint) {
                int start = s.indexOf('#');
                if (start != -1) {
                    EventQueue.invokeLater(() -> {
                        try {
                            int lineNumber = Integer.parseInt(s.substring(start + 1)) - 1;
                            ApplicationSettingsState service = ServiceManager.getService(ApplicationSettingsState.class);
                            EffectType type = Arrays.stream(EffectType.values()).filter(a -> a.name().equals(service.effectStyle)).findFirst().orElse(null);
                            RangeHighlighter highlighter = markupModel.addLineHighlighter(lineNumber, 10, new TextAttributes(service.enableForegroundColor ? new Color(service.foregroundColor) : null, service.enableBackgroundColor ? new Color(service.backgroundColor) : null, service.enableEffectColor ? new Color(service.effectColor) : null, service.enableEffectColor ? type : null, 0));
                            highlighters.add(highlighter);
                        } catch (Exception e) {
                            logger.info("parse trace error with str={}", s);
                        }
                    });
                }
                consoleView.print(s, ConsoleViewContentType.SYSTEM_OUTPUT);
            } else if (s.startsWith(START_WORD)) {
                consoleView.print(agentId + LISTENING_STR, ConsoleViewContentType.SYSTEM_OUTPUT);
                isStartPrint = true;
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info(agentId + WEBSOCKET_CLOSE);
        consoleView.print(agentId + WEBSOCKET_CLOSE + ENTER, ConsoleViewContentType.SYSTEM_OUTPUT);
        EventQueue.invokeLater(() -> {
            for (RangeHighlighter highlighter : highlighters) {
                this.markupModel.removeHighlighter(highlighter);
            }
        });
    }

    @Override
    public void onError(Exception e) {
        //如果报错后也有业务写在这里
        logger.error(WEBSOCKET_ERROR, agentId, e);
        consoleView.print(e.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
    }


    @Override
    public void dispose() {
        close();
    }
}
