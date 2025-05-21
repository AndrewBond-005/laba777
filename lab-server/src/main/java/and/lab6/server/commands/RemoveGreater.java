package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

import java.util.Iterator;


public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, большие, чем заданный");
        this.collectionManager = collectionManager;
        this.console = console;
    }


    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        long id = collectionManager.findUser(request.login(), request.password());
        int k=0;
        Iterator<Worker> iterator = collectionManager.getCollection().iterator();
        while (iterator.hasNext()) {
            Worker w = iterator.next();
            if (w.compareTo(request.workers().get(0)) > 0) {
                k++;
                iterator.remove();
                if (collectionManager.remove(w, id)) {
                    k--;
                }
            }
        }
        //collectionManager.getCollection().removeIf(item -> item.compareTo(request.workers().get(0)) < 0);
        if(k==0) return new Response("Элементы успешно удалёны", null, 200);
        else return new Response("Не получилось удалитьб некоторые элемменты", null, 404);
    }
}
