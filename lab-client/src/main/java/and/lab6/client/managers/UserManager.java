package and.lab6.client.managers;

import and.lab6.client.utility.Console;
import and.lab6.client.utility.Execute;
import util.Action;
import util.Request;
import util.Response;

import java.util.List;

public class UserManager {

//    private String login;
//    private String password;
//
//    public UserManager( String login, String password) {
//
//        this.login = login;
//        this.password = password;
//    }

    private String login;
    private String password;
    private final Console console;
    private UDPManager udpManager;
    private Execute execute;

    public void setUdpManager(UDPManager udpManager) {
        this.udpManager = udpManager;
    }

    public void setExecute(Execute execute) {
        this.execute = execute;
    }

    public UserManager(Console console, UDPManager udpManager, Execute execute, String login, String password) {
        this.console = console;
        this.udpManager = udpManager;
        this.execute = execute;
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void singIn(String mode) {
        do {
            insertValues(mode);
            Response response = null;
            while (true) {
                response = execute.recieve();
                if (response != null) {
                    break;
                }
            }
            if (response.returnCode() != 200) {
               //console.printError( "Не удалось выполнить запрос. Проверьте корректность логина и пароля и попробуйте ещё раз.");
                continue;
            }
            break;
        } while (true);
    }

    public String autORReg() {
        console.print("Введите 'a' если хотите авторизоваться и 'r' если зарегистрироваться: ");
        String mode;
        do {
            mode = console.readln().trim();
            if (!mode.equals("a") && !mode.equals("r")) {
                console.println("не понял, повторите ввод");
                continue;
            }
            return mode;
        } while (true);
    }

    private void insertValues(String mode) {
        String login;
        do {
            console.print("Введите логин(от 1 до 25 символов): ");
            login = console.readln().trim();
            if (login.matches(".*\\s.*")) {
                console.println("В строке есть пробельные символы, пожалуйста, придумайте логин без них");
            }
            else if (login.length()>25){
                console.println("логин слищком длинный, придумайте покороче");
            }
            else if(!login.isEmpty())break;
        } while (true);
        String password;
        do {
            console.print("Введите пароль(от 3 символов): ");
            password = console.readln().trim();
            if (password.length() <= 3) {
                console.println("Пароль слишком короткий, придумайте подлинее");
            } else if (password.equals("1234")) {
                console.printError("Этот парль уже используется пользователем andrei.");
            } else break;
        } while (true);

        setLogin(login);
        setPassword(password);
        udpManager.send(new Request(null, null, null,
                mode.equals("a")?Action.AUTHORIZATION:Action.REGISTRATION,login,password));

    }


}
