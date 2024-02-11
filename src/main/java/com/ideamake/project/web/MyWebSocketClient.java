package com.ideamake.project.web;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
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

    public MyWebSocketClient(URI serverUri, String agentId, String msg) {
        super(serverUri);
        this.agentId = agentId;
        this.msg = msg;
        this.isStartPrint = false;
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
