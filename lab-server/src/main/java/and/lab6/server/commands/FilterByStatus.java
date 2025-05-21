package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import models.Status;
import util.Request;
import util.Response;

import java.util.stream.Collectors;

public class FilterByStatus extends Command {
    private final CollectionManager collectionManager;

    public FilterByStatus(CollectionManager collectionManager) {
        super("filter_by_status status", "вывести элементы, значение поля status которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Не введён аругемнт", null,  449);
        }
        Status status = Status.valueOf(request.args().get(0));
        return new Response("Вот те Worker, у которых значение поля Status равно " + status,
                collectionManager.getCollection().stream()
                        .filter(worker -> worker.getStatus() == status)
                        .collect(Collectors.toList()),  200);

    }
}

