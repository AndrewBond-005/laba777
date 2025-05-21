package and.lab6.client.commands;

import and.lab6.client.ask.AskBreak;
import and.lab6.client.ask.AskWorker;
import and.lab6.client.utility.Console;
import models.Worker;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class RemoveLower extends Command {
    private final Console console;

    public RemoveLower(Console console) {
        super("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        Worker worker = null;
        try {
            worker = AskWorker.askWorker(console, scriprtMode);
            if (worker == null) {
                return false;
            }
            if (worker.validate().isEmpty()) {
                console.println("Поля Worker корректны");
                return worker;
            } else {
                console.println("Поля Worker некорректны: " + worker.validate());
                return worker.validate();
            }
        } catch (AskBreak e) {
            console.printError("");
        } catch (NullPointerException e) {
            console.printError("Worker = null");
        }
        return false;

    }
}