package and.lab6.client.commands;

import and.lab6.client.utility.Console;
import models.Worker;

import java.util.Comparator;
import java.util.List;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class PrintFieldAscendingStatus extends Command {
    private final Console console;

    public PrintFieldAscendingStatus(Console console) {
        super("print_field_ascending_status", "вывести значения поля status всех элементов в порядке возрастания");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }

    public void execute(List<Worker> workers) {
        ;
        Comparator<Worker> comparator = new StatusComparator();
        workers.sort(comparator);
        for (Worker w : workers) {
            console.println(w.getStatus());
        }
    }

    public static class StatusComparator implements Comparator<Worker> {

        @Override
        public int compare(Worker o1, Worker o2) {
            if (o1.getStatus() != o2.getStatus()) return o1.getStatus().ordinal() - o2.getStatus().ordinal();
            return o1.compareTo(o2);
        }
    }
}