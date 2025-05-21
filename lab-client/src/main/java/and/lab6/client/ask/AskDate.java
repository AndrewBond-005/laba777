package and.lab6.client.ask;

import and.lab6.client.utility.Console;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

public class AskDate {
    public static LocalDate askDate(Console console, boolean scriprtMode) throws AskBreak {
        try {
            LocalDate endDate;
            while (true) {
                console.print("Введите значение даты окончания работы EndDate (Exemple: " +
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + " or 2025-05-25): ");
                var line = console.readln().trim();
                if (line.isEmpty())
                    continue;
                if (line.equals(console.getStopWord()) || line.equals(console.getExitWord()))
                    throw new AskBreak(line);
                if (line.isEmpty()) {
                    endDate = null;
                    break;
                }
                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        endDate = UpdateID.worker.getEndDate();
//                    } else {
                    endDate = LocalDate.parse(line, DateTimeFormatter.ISO_DATE);
//                    }
                    break;
                } catch (DateTimeParseException e) {
                    console.printError("Ошибка! Введённая строка не соответсвует формату даты");
                    if (scriprtMode) return null;
                }
            }
            //BackUp.println(String.valueOf(endDate));
            return endDate;
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
    }
}
