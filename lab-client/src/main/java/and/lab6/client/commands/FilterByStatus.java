package and.lab6.client.commands;

import and.lab6.client.utility.Console;
import models.Status;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class FilterByStatus extends Command {
    private final Console console;

    public FilterByStatus(Console console) {
        super("filter_by_status status", "вывести элементы, значение поля status которых равно заданному");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        try {
            Status status = Status.valueOf(arguments);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}
