package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

import java.time.LocalDateTime;


public class Info extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
        this.console = console;
    }


    @Override
    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();
return new Response("Сведения о коллекции:"+'\n'+
        (" Тип: " + collectionManager.getCollection().getClass())+'\n'+
        (" Количество элементов: " + (collectionManager.getCollection()==null?
                0:collectionManager.getCollection().size()))+'\n'+
        (" Дата последнего сохранения: " + lastSaveTimeString)+'\n'+
        (" Дата последней инициализации: " + lastInitTimeString)
                ,null,200);

    }

}
