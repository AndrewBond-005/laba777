package and.lab6.server.utility;

import and.lab6.server.managers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Action;
import util.Request;
import util.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private final DBUserManager dbUserManager;
    private final DBManager dbManager;
    private final DBWorkerManager dbWorkerManager;
    private final ExecutorService pool;
    private static final Logger logger = LogManager.getLogger(Execute.class);

    public Execute(CommandManager commandManager, Console console, UDPManager udpManager,
                   DBUserManager dbUserManager, DBManager dbManager, DBWorkerManager dbWorkerManager) {
        this.dbManager = dbManager;
        this.dbUserManager = dbUserManager;
        this.dbWorkerManager = dbWorkerManager;
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
        this.pool = Executors.newCachedThreadPool();
    }

    public void execute() {
        pool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    logger.debug("Ожидание нового пакета");
                    Object object = udpManager.receive();
                    if (object instanceof Request request) {
                        pool.submit(() -> {
                            try {
                                if (request.action() == Action.AUTHORIZATION || request.action() == Action.REGISTRATION) {
                                    udpManager.somethingWithClient(request, dbUserManager);
                                    return;
                                }
                                if (dbUserManager.verifyUser(request.login(), request.password()) == -1) {
                                    logger.info("Нераспознали пользователя " + request.login());
                                    udpManager.send(new Response("Вы не авторизовались", null, 404, Action.FAIL));
                                    return;
                                }
                                String commandName = request.command();
                                var command = commandManager.getCommands().get(commandName);
                                if (command == null) {
                                    logger.warn("Команда {} не найдена", commandName);
                                    udpManager.send(new Response("Команда не найдена", null, 404, Action.FAIL));
                                    return;
                                }
                                var response = command.execute(request);
                                logger.info("Команда выполнена, сформирован ответ");
                                udpManager.send(response);
                            } catch (Exception e) {
                                logger.error("Ошибка обработки запроса", e);
                                udpManager.send(new Response("Ошибка сервера", null, 500, Action.FAIL));
                            }
                        });
                    } else if (object instanceof Action action) {
                        pool.submit(() -> udpManager.somethingWithClient(
                                new Request(null, null, null, action), dbUserManager));
                    }
                } catch (Exception e) {
                    if (Thread.currentThread().isInterrupted()) {
                        logger.info("Прием запросов прерван");
                        return;
                    }
                    logger.error("Ошибка при получении пакета", e);
                }
            }
        });
    }

    public void shutdown() {
        pool.shutdownNow();
        udpManager.shutdown();
    }
}