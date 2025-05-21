package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Action;
import util.Request;
import util.Response;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.concurrent.*;

public class UDPManager {
    private final int port;
    private final ReceivingManager receivingManager;
    private final SendingManager sendingManager;
    private final HashSet<InetSocketAddress> sessions = new HashSet<>();
    private final DatagramSocket socket;
    private final ExecutorService readPool;
    private final ExecutorService sendPool;
    private static final Logger logger = LogManager.getLogger(UDPManager.class);

    public UDPManager(int port, SendingManager sendingManager, ReceivingManager receivingManager) throws IOException {
        this.port = port;
        this.sendingManager = sendingManager;
        this.receivingManager = receivingManager;
        this.readPool = Executors.newFixedThreadPool(5);
        this.sendPool = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            logger.error("Ошибка при подключении порта", e);
            throw e;
        }
    }

    public int getPort() {
        return port;
    }

    public HashSet<InetSocketAddress> getSessions() {
        return sessions;
    }



    public void startReceiving(Runnable RequestReceived) {
        readPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object obj = receivingManager.receive(port, socket);
                    if (obj != null) {
                        RequestReceived.run();
                    }
                } catch (Exception e) {
                    logger.error("Ошибка при получении запроса", e);
                }
            }
        });
    }

    public Object receive() {
        return receivingManager.receive(port, socket);
    }

    public void send(Object object) {
        sendPool.submit(() -> {
            try {
                sendingManager.send(port, receivingManager.lastReceivedAddress, object, socket);
            } catch (Exception e) {
                logger.error("Ошибка при отправке ответа", e);
            }
        });
    }

    public void sendAll(Object object) {
        sendPool.submit(() -> {
            for (InetSocketAddress address : sessions) {
                try {
                    sendingManager.send(port, address, object, socket);
                } catch (Exception e) {
                    logger.error("Ошибка при отправке на {}", address, e);
                }
            }
        });
    }

    public void addAddress(InetSocketAddress address) {
        synchronized (sessions) {
            sessions.add(address);
        }
    }

    public void deleteAddress(InetSocketAddress address) {
        synchronized (sessions) {
            sessions.remove(address);
        }
    }

    public void somethingWithClient(Request request, DBUserManager dbUserManager) {
        Action action = request.action();
        if (action == Action.CLIENT_CONNECTS) {
            addAddress(receivingManager.lastReceivedAddress);
            logger.info("Подключился новый клиент с {}", receivingManager.lastReceivedAddress);
            send(Action.SERVER_CONNECTS);
        } else if (action == Action.CLIENT_DISCONNECTS) {
            deleteAddress(receivingManager.lastReceivedAddress);
            logger.info("Клиент с адреса {} отключился", receivingManager.lastReceivedAddress);
        } else if (action == Action.AUTHORIZATION) {
            Response response = dbUserManager.autUser(request.login(), request.password());
            logger.info(response.action() != Action.FAIL ? "Авторизация успешна" : "Ошибка авторизации");
            send(response);
        } else if (action == Action.REGISTRATION) {
            Response response = dbUserManager.regUser(request.login(), request.password());
            logger.info(response.action() != Action.FAIL ? "Регистрация успешна" : "Ошибка регистрации");
            send(response);
        }
    }

    public void shutdown() {
        logger.info("Завершение работы UDPManager");
        readPool.shutdownNow();
        sendPool.shutdown();
        try {
            if (!readPool.awaitTermination(1, TimeUnit.SECONDS)) {
                logger.warn("readPool не завершился вовремя");
            }
            if (!sendPool.awaitTermination(1, TimeUnit.SECONDS)) {
                logger.warn("sendPool не завершился вовремя");
            }
        } catch (InterruptedException e) {
            logger.error(e);
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
            logger.info("Сокет закрыт");
        }
    }
}