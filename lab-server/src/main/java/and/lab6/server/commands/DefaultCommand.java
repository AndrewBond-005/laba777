package and.lab6.server.commands;


import util.Request;
import util.Response;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class DefaultCommand extends Command {
    public DefaultCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public Response execute(Request request) {
        return null;
    }

}
