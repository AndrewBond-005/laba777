package and.lab6.client.commands;

import and.lab6.client.managers.CommandManager;
import and.lab6.client.utility.Console;
import models.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

///execute_script - с клиента прислыается команда exe sc
/// а аргументы это список команд с аргументами.
/// а если волженные скирпты - инлайнить их содержимое в родительские скрипты
/// на сервере будет брать элементы из args
///
///
public class ExecuteScript extends Command {
    private final Console console;
    private final CommandManager commandManager;
    private HashMap<String, Integer> map = new HashMap<>();
    private boolean stop = false;
    private final List<String> args = new ArrayList<>();
    private final List<Worker> workers = new ArrayList<>();

    public ExecuteScript(Console console, CommandManager commandManager) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте" +
                " содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.console = console;
        this.commandManager = commandManager;
    }

    private String enterScriptName(String arguments) {
        String name = arguments;
        if (arguments == null || arguments.isEmpty() || arguments.equals(" ")) {
            console.print("Введите название скрипта: ");
            name = console.readln().trim();
        }
        return name;
    }


    public List<Worker> getWorkers() {
        return workers;
    }

    public List<String> getArgs() {
        return args;
    }

    public int execute(String arguments, boolean scriptMode) {
        args.clear();
        var name = enterScriptName(arguments);
        if (map.get(name) == null) {
            map.put(name, 1);
        } else {
            map.put(name, map.get(name) + 1);
        }
        console.println(name + " " + map.get((name)));
        if (map.get(name) > commandManager.getMaxRecursionDeep()) {
            console.println("Достигнут лимит глубины рекурсиии.");
            //console.println(name+" -1"+"  №1");
            map.put(name, map.get(name) - 1);
            return 0;
        }
        console.println("Начинается обработка скрипта " + name);
        try (Scanner scriptScanner = new Scanner(new File(name))) {
            if (!scriptScanner.hasNextLine()) throw new NoSuchElementException();
            console.setFileScanner(scriptScanner);
            //exit = false;///////
            // убрать комментарии чтобы выходить только из скрипта,
            // где написана эта строчка
            while (console.isCanReadln() && !stop) {
                // console.selectConsoleScanner();
                var line = console.readln().trim();
                String[] tokens = line.split(" ", 2);
                if (line.isEmpty())
                    continue;
                var command = commandManager.getCommands().get(tokens[0]);
                if (arguments.equals(console.getStopWord()) || arguments.equals(console.getExitWord())) {
                    console.println("Завершение чтения команд из-за ввода " + arguments);
                    //console.println(name+" -1"+"  №2");
                    map.put(name, map.get(name) - 1);
                    // result.append(tokens[0]).append('\n');
                    return 1;
                }
                if (tokens[0].equals(console.getStopWord()) || tokens[0].equals(console.getExitWord())) {
                    console.println("Завершение чтения команд из-за ввода " + tokens[0]);
                    //console.println(name+" -1"+"  №3");
                    map.put(name, map.get(name) - 1);
                    //result.append(tokens[0]).append('\n');
                    return 1;
                }
                if (command == null) {
                    console.printError("Команды " + tokens[0] + " не обнаружено.");
                    return -1;
                }
                console.setRepeatMode(true);
                if (!tokens[0].equals("es")) {
                    Object res = command.validate(tokens.length > 1 ? tokens[1] : null, true);
                    if (res instanceof Worker) {
                        if (tokens.length > 1) {
                            args.add(tokens[0] + " " + tokens[1] + " worker");
                        } else {
                            args.add(tokens[0] + " worker");
                        }
                        workers.add((Worker) res);
                    } else if (res instanceof Boolean) {
                        if (!(Boolean) res) {
                            console.printError("ощибочка какая-то");
                            return -1;
                        }
                        if (tokens.length > 1) {
                            args.add(tokens[0] + " " + tokens[1]);
                        } else {
                            args.add(tokens[0]);
                        }
                    } else {
                        console.printError("Аргументы не валидны");
                        console.printError(res);
                        return -1;
                    }
                    //  console.println(name+" -1"+"  №4");
                    //  map.put(name, map.get(name) - 1);
                } else {
                    if (tokens.length == 1) {
                        var script = enterScriptName(null);
                        List<String> tokenList = new ArrayList<>(Arrays.asList(tokens));
                        tokenList.add(script);
                        tokens = tokenList.toArray(new String[0]);
                    }
                    int s = this.execute(tokens.length > 1 ? tokens[1] : null, true);

                    //console.println(name+" -1"+"  №5");
                    // map.put(name, map.get(name) - 1);
                    if (s != 0) {
                        return s;
                    }
                }

                console.setFileScanner(scriptScanner);
            }
            // console.println(name+" -1"+"  №6");
            map.put(name, map.get(name) - 1);

            return 0;
        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        }
        return 0;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return false;
    }
}
