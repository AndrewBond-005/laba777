package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import util.Request;
import util.Response;

import java.util.Collections;


public class MinByPosition extends Command {
    private final CollectionManager collectionManager;

    public MinByPosition(CollectionManager collectionManager) {
        super("min_by_position", "вывести любой объект из коллекции, значение поля position которого является минимальным");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        var workers = collectionManager.getCollection();
        return new Response("Вот Worker с минимальным Position",
                Collections.singletonList(workers.stream()
                        .filter(worker -> worker.getPosition().ordinal() == 0)
                        .findFirst()
                        .orElse(null)), 200);
    }

}
