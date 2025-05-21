package and.lab6.server.utility;

import java.util.Scanner;

/**
 * Консоль для ввода команд и вывода результата
 */
public interface Console {
    public String getStopWord();

    public String getExitWord();

    void print(Object obj);

    void println(Object obj);

    String readln();

    boolean isCanReadln();

    void printError(Object obj);

    void setFileScanner(Scanner scanner);

    Scanner getFileScanner();

    void selectConsoleScanner();

    void setRepeatMode(boolean repeatMode);
}