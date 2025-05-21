package and.lab6.client.utility;

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

    boolean hasInput();

    void selectConsoleScanner();

    void setRepeatMode(boolean repeatMode);
}