package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Show extends Command {
    private final Console console;

    public Show(Console console) {
        super("show", "вывести в стандартный поток вывода все" +
                " элементы коллекции в строковом представлении");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}