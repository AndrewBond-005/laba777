package and.lab6.server.commands;

import and.lab6.server.managers.CommandManager;
import and.lab6.server.managers.UDPManager;
import util.Request;
import util.Response;

import java.util.HashMap;
import java.util.Map;


public class Help extends Command {
    private final CommandManager commandManager;
    private final UDPManager udpManager;

    public Help(CommandManager commandManager, UDPManager udpManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = commandManager;
        this.udpManager = udpManager;
    }


    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null,  450);
        }
        var commands = commandManager.getCommands();
        Map<String, Command> com = new HashMap<>();
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            str.append(entry.getKey()).append('\t').append('\t')
                    .append(entry.getValue().getDescription()).append('\n');
        }

        // send(com);
        // Map<String, Command> commands = commandManager.getCommands();
//        for (Command command : commands.values()) {
//            System.out.printf(" %-35s%-1s%n", command.getName(), command.getDescription());
//        }
        return new Response(str.toString(),null,200);
    }

}
