package and.lab6.client.utility;

import and.lab6.client.commands.PrintFieldAscendingStatus;
import and.lab6.client.managers.CollectionManager;
import and.lab6.client.managers.CommandManager;
import and.lab6.client.managers.UDPManager;
import and.lab6.client.managers.UserManager;
import util.Action;
import util.Response;

public class ResponseProcessing {
    private final Console console;
    private UserManager userManager;
    private Execute execute;
    private CommandManager commandManager;
    private CollectionManager collectionManager;

    public void setExecute(Execute execute) {
        this.execute = execute;
    }

    public ResponseProcessing(Console console, Execute execute,
                              UserManager userManager, CommandManager commandManager, CollectionManager collectionManager) {
        this.console = console;
        this.execute = execute;
        this.userManager = userManager;
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public void answerIsProgrammStatus(Action action) {
        if (action == Action.SERVER_DISCONNECTS) {
            console.println("сервер недоступен");
            execute.setServerAvailable(false);
        }
        if (action == Action.SERVER_CONNECTS) {
            console.println("сервер доступен");
            execute.setServerAvailable(true);
        }
    }

    public void answerIsResponse(Response response) {
//        System.out.println("==="+response.message() + '\n' + "==="+
//                ((response.workers()==null)?"0":response.workers().get(0)) + '\n' + "==="+
//                ((response.workers()==null)?"0":response.workers().size()) + '\n' + "==="+
//                response.returnCode());
        if (response.action() != null && response.action() == Action.FAIL) {
            userManager.setLogin("");
            userManager.setPassword("");
            console.printError(response.message());
            return;
        }
        if (response.returnCode() < 0) {
            execute.setPacketCount(-response.returnCode());
            return;
        }
        if (response.returnCode() == 1001) {
            assert response.workers() != null;
            ((PrintFieldAscendingStatus) commandManager.
                    getCommands().get("print_field_ascending_status")).
                    execute(response.workers());
            return;
        }
        if (response.returnCode() == 1000)
            console.print(response.message());
        else if (response.returnCode() != 200 && response.returnCode() > 0)
            console.printError(response.message());
        else if (response.returnCode() != 0)
            console.println(response.message());

        if (response.workers() != null && response.returnCode() == 0) {
            if (collectionManager.getCollection().isEmpty()) {
                console.println("Вот первые 100 элементов коллекции:");
                for (var w : response.workers()) {
                    console.println(w);
                }
                console.println("Продолжаем принимать сообщения с содержимым коллекции от сервера");
            }
            for (var w : response.workers()) {
                collectionManager.add(w);
            }
        } else if (response.workers() != null && response.returnCode() != 0) {
            for (var w : response.workers()) {
                console.println(w);
            }
        }
    }
}
