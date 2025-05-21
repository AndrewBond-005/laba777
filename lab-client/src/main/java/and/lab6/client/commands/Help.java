package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Help extends Command {
    private final Console console;

    public Help(Console console) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}
