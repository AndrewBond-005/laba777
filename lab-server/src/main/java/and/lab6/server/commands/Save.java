package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import util.Request;
import util.Response;


public class Save extends Command {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        collectionManager.saveCollection();
        //BackUp.clear();
        return null;
    }

}