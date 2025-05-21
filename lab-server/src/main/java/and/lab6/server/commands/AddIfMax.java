package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import models.Worker;
import util.Request;
import util.Response;

import java.util.Comparator;
import java.util.TreeSet;

public class AddIfMax extends Command {
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение " +
                "превышает значение наибольшего элемента этой коллекции");

        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null,  450);
        }

        Worker a = (request.workers().get(0));
        a.setId(collectionManager.getFreeId());

        if (collectionManager.getCollection().isEmpty()) {
            collectionManager.add(a, collectionManager.findUser(request.login(), request.password()));
            return new Response("Worker успешно добавлен", null, 200);
        }
        Comparator<Worker> comparator = new NameComparator();
        TreeSet<Worker> workers = new TreeSet<>(comparator);
        workers.addAll(collectionManager.getCollection());
        Worker maxi = (workers).last();
        maxi.setName(maxi.getName().toLowerCase());
        Worker b = a;
        b.setName(b.getName().toLowerCase());
        int result = comparator.compare(maxi, b);
        if (result < 0) {
            if (Runtime.getRuntime().freeMemory() < 10_000_000) {
                return new Response("обавление может привести к переполнению памяти и вылету программы.",
                        null, 451);
            }
            if (collectionManager.add(a, collectionManager.findUser(request.login(), request.password()))){

                return new Response("Worker успешно добавлен", null, 200);}
            else
                return new Response("Worker не добавлен", null, 409);
        }
        return new Response("Не удалось добавить Worker, он меньше максимального",
                null,  200);
    }

    public static class NameComparator implements Comparator<Worker> {
        @Override
        public int compare(Worker o1, Worker o2) {
            if (o1.getName().compareTo(o2.getName()) != 0) return o1.getName().compareTo(o2.getName());
            if (o1.getSalary() != o2.getSalary()) return Integer.compare(o1.getSalary(), o2.getSalary());
            return o1.compareTo(o2);
        }
    }
}
