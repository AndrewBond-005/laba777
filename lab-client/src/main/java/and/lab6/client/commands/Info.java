package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Info extends Command {
    private final Console console;

    public Info(Console console) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}
