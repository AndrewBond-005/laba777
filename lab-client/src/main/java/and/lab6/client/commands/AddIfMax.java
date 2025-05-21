package and.lab6.client.commands;

import and.lab6.client.ask.AskBreak;
import and.lab6.client.ask.AskWorker;
import and.lab6.client.utility.Console;
import models.Worker;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class AddIfMax extends Command {
    private final Console console;

    public AddIfMax(Console console) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение " +
                "превышает значение наибольшего элемента этой коллекции");
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
