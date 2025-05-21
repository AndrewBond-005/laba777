package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.managers.CommandManager;
import and.lab6.server.managers.UDPManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

///execute_script - с клиента прислыается команда exe sc
/// а аргументы это список команд с аргументами.
/// а если волженные скирпты - инлайнить их содержимое в родительские скрипты
/// на сервере будет брать элементы из args
/// если exit то выходить из скрипта
///
public class ExecuteScript extends Command {
    private final CollectionManager collectionManager;
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private HashMap<String, Integer> map = new HashMap<>();
    private boolean stop = false;

    public ExecuteScript(Console console, CollectionManager collectionManager, CommandManager commandManager, UDPManager udpManager) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте" +
                " содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.collectionManager = collectionManager;
        this.console = console;
        this.commandManager = commandManager;
        this.udpManager = udpManager;
    }

    @Override
    public Response execute(Request request) {
        int i = 0;
        StringBuilder message = new StringBuilder();
        List<Worker> workers = new ArrayList<>();
        String script = null;
        int returnCode = 200;
        for (var elem : request.args()) {
            String[] tokens = elem.split(" ");
            Worker worker = null;
            if (tokens[tokens.length - 1].equals("worker")) {
                worker = request.workers().get(i);
                i++;
            }
            var command = commandManager.getCommands().get(tokens[0]);
            List<Worker> w = new ArrayList<>();
            w.add(worker);
            Request req = new Request(tokens[0],
                            (tokens.length>1)?(!tokens[1].equals("worker"))?
                                    Collections.singletonList(tokens[1]) :null:null,
                    w,null,request.login(), request.password());
            //System.out.println(commandManager.getCommands());

            Response response = command.execute(req);

            message.append(response.message()).append(" ");
            if (response.workers() != null) {
                workers.addAll(response.workers());
            }
            returnCode=Math.max(returnCode,response.returnCode());
        }
        var answer= new Response(message.toString(), workers, returnCode);

        return answer;
    }
}
