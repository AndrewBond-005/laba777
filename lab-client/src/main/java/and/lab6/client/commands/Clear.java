package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Clear extends Command {
    private final Console console;

    public Clear(Console console) {
        super("clear", "очистить коллекцию");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}
