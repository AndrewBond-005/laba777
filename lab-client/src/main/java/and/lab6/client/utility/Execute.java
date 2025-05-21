package and.lab6.client.utility;

import and.lab6.client.commands.ExecuteScript;
import and.lab6.client.managers.CollectionManager;
import and.lab6.client.managers.CommandManager;
import and.lab6.client.managers.UDPManager;
import and.lab6.client.managers.UserManager;
import models.Worker;
import util.Action;
import util.Request;
import util.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private boolean serverAvailable = false;
    private final long RECEIVE_TIMEOUT = 150; // 100ms
    private CollectionManager collectionManager;
    private int packetCount = 0;
    private List<Request> requests = new ArrayList<>();
    private ResponseProcessing responseProcessing;
    private final UserManager userManager;


    public Execute(CommandManager commandManager, Console console, UDPManager udpManager,
                   UserManager userManager, ResponseProcessing responseProcessing, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
        this.userManager = userManager;
        this.responseProcessing = responseProcessing;
        this.collectionManager = collectionManager;
    }

    public boolean isServerAvailable() {
        return serverAvailable;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public void setServerAvailable(boolean serverAvailable) {
        this.serverAvailable = serverAvailable;
    }

    public void connect() {
        udpManager.send(Action.CLIENT_CONNECTS);
        console.println("Пробуем подключиться к серверу");
        var currentDate = System.currentTimeMillis();
        int i = 1;
        while (!serverAvailable) {
            if (System.currentTimeMillis() - currentDate > 5000) {
                if (i == 1) {
                    console.println("Похоже, сервер сильно загружен или недоступен. ");
                    console.println("Попробуйте подключиться позже или ожидайте подтверждение подключения от сервера");
                }
                i = 0;
                currentDate = System.currentTimeMillis();
                udpManager.send(Action.CLIENT_CONNECTS);
            }
            Object response = udpManager.receive(RECEIVE_TIMEOUT);
            if (response instanceof Action) {
                responseProcessing.answerIsProgrammStatus((Action) response);
                break;
            }
        }
    }

    public void execute() {
        connect();
        console.println("Успешно подключились к серверу");
        userManager.singIn(userManager.autORReg());
        while (true) {
            recieve();
            if (userManager.getLogin().isEmpty() || userManager.getPassword().isEmpty()) {
                userManager.singIn(userManager.autORReg());
                continue;
            }
            if (!requests.isEmpty() && serverAvailable) {
                while (!requests.isEmpty()) {
                    if (requests.get(0) != null) {
                        send(requests.get(0));
                    }
                    requests.remove(0);
                }
            }
            if (!collectionManager.getCollection().isEmpty() && packetCount == 0) {
                console.println("Введите yes если хотите увидеть все элементы коллекции");
            }
            console.print(">");
            console.selectConsoleScanner();
            var line = console.readln().trim();
            if (line.isEmpty()) {
//                if(!serverAvailable) {
//                    connect();
//                }
                continue;}
            String[] tokens = line.split(" ", 2);
            var command = commandManager.getCommands().get(tokens[0]);
            if (line.equals("yes") && !collectionManager.getCollection().isEmpty() && packetCount == 0) {
                for (Worker w : collectionManager.getCollection()) {
                    console.println(w);
                }
                collectionManager.removeAll();
            } else {
                collectionManager.removeAll();

                if (command == null) {
                    console.printError("Команда не распознана");
                    continue;
                }
                Request request = executeCommand(tokens);
                if (serverAvailable) {
                    if (request != null) {
                        send(request);
                    }
                } else {
                    console.println("Сервер сейчас недоступен.");
                    console.println("Ваша команда выполнится когда серевер станет доступным");
                    requests.add(request);
                }
            }
        }
    }

    public Request executeCommand(String[] tokens) {
        tokens[0] = tokens[0].trim();
        if (tokens.length > 1)
            tokens[1] = tokens[1].trim();
        Request request = null;
        if (tokens[0].equals("es")) {
            request = (Request) executeScipt(tokens);
        } else if (tokens[0].equals("exit")) {
            System.exit(0);
        } else {
            var res = notExecuteScript(tokens);
            if (res instanceof Request) {
                request = (Request) res;
            } else if (res instanceof String) {
                console.println(res);
            }
        }
        return request;
    }

    public Response recieve() {
        Response resp = null;
        if (packetCount == 0) packetCount++;
        if (packetCount > 0) {
            int i = 2;
            do {
                Object response = udpManager.receive(RECEIVE_TIMEOUT);
                Object eww = response;
                try {
                    resp = (Response) response;
                } catch (Exception e) {
                    resp = new Response(null, null, 1, (Action) eww);
                }
                // System.out.println("f" + String.valueOf(i));
                if (response != null) {
                    //System.out.println(response);
                    i = 2;
                    packetCount--;
                    if (response instanceof Response) {
                        responseProcessing.answerIsResponse((Response) response);
                    } else if (response instanceof Action) {
                        responseProcessing.answerIsProgrammStatus((Action) response);
                    } else {
                        console.println("Получен неизвестный объект: " +
                                response.toString());
                    }
                } else {
                    i--;
                }
                if (packetCount < 0) {
                    packetCount = 0;
                }
            } while (packetCount > 0 && i > 0);
        }
        return resp;
    }

    private void send(Request request) {
        do {
            if (serverAvailable) {
                //console.println(request);
                udpManager.send(new Request(request.command(), request.args(), request.workers(),
                        request.action(), userManager.getLogin(), userManager.getPassword()));
                break;
            }
            recieve();
        } while (true);
    }

    public Object executeScipt(String[] tokens) {
        int res = ((ExecuteScript) commandManager.getCommands().get("es")).
                execute(tokens.length > 1 ? tokens[1] : null, true);
        if (res == -1) {
            console.printError("");
            return null;
        } else {
            var es = ((ExecuteScript) commandManager.getCommands().get("es"));
            var request = new Request("es", es.getArgs(), es.getWorkers());
            return request;
        }
    }

    public Object notExecuteScript(String[] tokens) {
        var command = commandManager.getCommands().get(tokens[0]);
        var val = command.validate(tokens.length > 1 ? tokens[1] : null, false);
        if (val instanceof Boolean) {
            if ((Boolean) val)
                return new Request(tokens[0], tokens.length > 1 ?
                        Collections.singletonList((tokens[1])) : null, null);
            else {
                return null;
            }
        } else if (val instanceof String) {
            return (val.toString());
        } else {
            var list = new ArrayList<Worker>();
            list.add((Worker) val);
            return new Request(tokens[0], tokens.length > 1 ?
                    Collections.singletonList(tokens[1]) : null, list);
        }
    }

}
/// //
//public void singIn(String mode) {
//    do {
//        insertValues(mode);
//        recieve();
//        if (userManager.getLogin().isEmpty() || userManager.getPassword().isEmpty()) {
//            console.println(
//                    "Не удалось выполнить запрос. Проверьте корректность логина и пароля и попробуйте ещё раз.");
//            continue;
//        }
//        break;
//    } while (true);
//}
//
//private String autORReg() {
//    console.print("Введите 'a' если хотите авторизоваться и 'r' если зарегистрироваться: ");
//    String mode;
//    do {
//        mode = console.readln().trim();
//        if (!mode.equals("a") && !mode.equals("r")) {
//            console.println("не понял, повторите ввод");
//            continue;
//        }
//        return mode;
//    } while (true);
//}
//
//private void insertValues(String mode) {
//    String login;
//    do {
//        console.print("Введите логин: ");
//        login = console.readln().trim();
//        if (!login.matches(".*\\s.*")) {
//            console.println("В строке есть пробельные символы, пожалуйста, придумайте логин без них");
//        }
//        else  break;
//    } while (true);
//    String password;
//    do {
//        console.print("Введите пароль: ");
//        password = console.readln().trim();
//        if (password.length() <= 3){
//            console.println("Пароль слишком короткий, придумайте подлинее");
//        }
//        else break;
//    }while(true);
//    userManager.setLogin(login);
//    userManager.setPassword(password);
//    if (mode.equals("a")) {
//        send(new Request("", List.of("login", "password"), null, Action.AUTHORIZATION));
//    } else {
//        send(new Request("", List.of("login", "password"), null, Action.REGISTRATION));
//    }
//}
///