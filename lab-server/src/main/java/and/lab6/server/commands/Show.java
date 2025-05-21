package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


public class Show extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все" +
                " элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.console = console;
    }
    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        var collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            return new Response("Коллекция пуста", null, 200);
        }
        return new Response("Вот содержмое коллекции:" + '\n',
                new ArrayList<>(collectionManager.getCollection()).stream()
                        .sorted(Comparator.comparing(Worker::getId))
                        .collect(Collectors.toList()), 1000);
    }

}
