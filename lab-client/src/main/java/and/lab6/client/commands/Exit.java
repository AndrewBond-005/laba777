package and.lab6.client.commands;

import and.lab6.client.utility.Console;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "выход из программы");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }

    public void execute() {
        System.exit(0);
    }
}
