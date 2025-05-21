package and.lab6.server;

import and.lab6.server.commands.*;
import and.lab6.server.managers.*;
import and.lab6.server.utility.Execute;
import and.lab6.server.utility.StandardConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        StandardConsole console = new StandardConsole();
        UDPManager udpManager = null;
        int port = 1200;
        while (port <= 65000) {
            try {
                udpManager = new UDPManager(port, new SendingManager(), new ReceivingManager());
                logger.info("Сервер открыт на порту: {}", port);
                break;
            } catch (IOException e) {
                logger.info("Порт {} занят, пробуем следующий", port);
                port++;
            }
        }
        DBManager dbManager = new DBManager(
                "jdbc:postgresql://192.168.10.80:5432/studs", "sXXXXXX",
                "your password from db studs, for example: 2DgU6akNiL7fRXqW(I change my real password :) )");
        dbManager.start();
        DBWorkerManager dbWorkerManager = new DBWorkerManager(dbManager);
        DBUserManager dbUserManager = new DBUserManager(dbManager);
        CollectionManager collectionManager = new CollectionManager(dbWorkerManager, dbUserManager);
        Map<String, Command> commands = new HashMap<>();
        CommandManager commandManager = new CommandManager();
        commands.put("add", new Add(collectionManager));
        commands.put("add_if_max", new AddIfMax(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("filter_by_status", new FilterByStatus(collectionManager));
        commands.put("generate", new Generate(collectionManager));
        commands.put("help", new Help(commandManager, udpManager));
        commands.put("info", new Info(console, collectionManager));
        commands.put("exit", new Exit(console));
        commands.put("remove_by_id", new RemoveById(console, collectionManager));
        commands.put("show", new Show(console, collectionManager));
        commands.put("print_field_ascending_status", new PrintFieldAscendingStatus(console, collectionManager));
        commands.put("min_by_position", new MinByPosition(collectionManager));
        commands.put("update_id", new UpdateID(console, collectionManager));
        commands.put("remove_lower", new RemoveLower(console, collectionManager));
        commands.put("remove_greater", new RemoveGreater(console, collectionManager));
        commands.put("es", new ExecuteScript(console, collectionManager, commandManager, udpManager));
        commandManager.setCommands(commands);
        collectionManager.loadCollection(); // Загрузка из БД
        if (udpManager != null) {
            logger.info("Отправляем всем известным клиентам, что сервер доступен");
            udpManager.sendAll(Action.SERVER_CONNECTS);
        }
        Execute execute = new Execute(commandManager, console, udpManager, dbUserManager, dbManager, dbWorkerManager);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Завершение работы сервера");
            execute.shutdown();
        }));
        execute.execute();
    }
}