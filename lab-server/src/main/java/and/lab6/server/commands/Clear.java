package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import util.Request;
import util.Response;

public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        if(collectionManager.removeAll(collectionManager.findUser(request.login(),request.password()))){
            return new Response("Все ваши элементы удалены", null, 200);
        }
        else return new Response("Не полуичлось удалить все ваши элементы или их и не было",
                null, 404);

    }
}
