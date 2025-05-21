package and.lab6.server.utility;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Для ввода команд и вывода результата
 */
public class StandardConsole implements Console {
    private static final String P = "$ ";
    private static Scanner fileScanner = null;
    private static final Scanner defScanner = new Scanner(System.in);
    private static boolean repeatMode = false;

    @Override
    public String getStopWord() {
        return "stop";
    }

    @Override
    public String getExitWord() {
        return "exit";
    }

    public void print(Object obj) {
        System.out.print((obj.toString()));
    }

    public void println(Object obj) {
        System.out.println((obj.toString()));
    }

    public void printError(Object obj) {
        System.err.println("Error: " + obj + '\n' + "одна ошибка и ты ошибся");

    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        String s;
        if (isCanReadln()) {
            s = (fileScanner != null ? fileScanner : defScanner).nextLine();
            // BackUp.println(s);

        } else {
            selectConsoleScanner();
            if (!isCanReadln()) {
                System.exit(0);
            }
            s = (fileScanner != null ? fileScanner : defScanner).nextLine();
            // BackUp.println(s);

        }
        if (fileScanner == null) {

        }
        if (repeatMode) println(s);
        return s;
    }

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    public void setFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    @Override
    public Scanner getFileScanner() {
        return fileScanner;
    }

    public void selectConsoleScanner() {
        fileScanner = null;
    }

    public void setRepeatMode(boolean repeatMode) {
        StandardConsole.repeatMode = repeatMode;
    }
}

