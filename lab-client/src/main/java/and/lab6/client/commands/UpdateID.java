package and.lab6.client.commands;

import and.lab6.client.ask.AskBreak;
import and.lab6.client.ask.AskWorker;
import and.lab6.client.utility.Console;
import models.Worker;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class UpdateID extends Command {
    private final Console console;

    public UpdateID(Console console) {
        super("update_id {id}", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        try {
            Integer.parseInt(arguments.trim());
        } catch (Exception e) {
            return false;
        }
        Worker worker = null;
        try {
            worker = AskWorker.askWorker(console, scriprtMode);
            if (worker == null) {
                return false;
            }
            if (worker.validate().isEmpty()) {
                return worker;
            } else {
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