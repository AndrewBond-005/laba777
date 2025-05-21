package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;


public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "выход из программы");
        this.console = console;
    }


    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        System.out.println("Выход из программы");
        ///BackUp.remove_last();
        System.exit(0);
        return null;
    }

}
