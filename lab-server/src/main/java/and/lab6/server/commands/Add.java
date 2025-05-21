package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import models.Worker;
import util.Request;
import util.Response;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        if (Runtime.getRuntime().freeMemory() < 10_000_000) {
            return new Response("обавление может привести к переполнению памяти и вылету программы.",
                    null, 451);
        }
        Worker a = (request.workers().get(0));
        a.setId(collectionManager.getFreeId());
        if (collectionManager.add(a, collectionManager.findUser(request.login(), request.password()))){
  
            return new Response("Worker успешно добавлен", null, 200);}
        else
            return new Response("Worker не добавлен", null, 409);
    }

}
