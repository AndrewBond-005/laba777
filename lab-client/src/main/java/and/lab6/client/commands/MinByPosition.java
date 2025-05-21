package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class MinByPosition extends Command {
    private final Console console;

    public MinByPosition(Console console) {
        super("min_by_position", "вывести любой объект из коллекции, значение поля position которого является минимальным");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}

