package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class RemoveById extends Command {
    private final Console console;

    public RemoveById(Console console) {
        super("remove_by_id {element}", "удалить элемент из коллекции по его id");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriptMode) {
        try {
            Integer.parseInt(arguments.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

