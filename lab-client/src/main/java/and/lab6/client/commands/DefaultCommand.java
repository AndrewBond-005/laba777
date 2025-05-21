package and.lab6.client.commands;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class DefaultCommand extends Command {
    public DefaultCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}
