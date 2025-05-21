package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Generate extends Command {
    private final Console console;

    public Generate(Console console) {
        super("generate {count}", "добавить новых элементов в коллекцию");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        try {
            Integer.parseInt(arguments.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
