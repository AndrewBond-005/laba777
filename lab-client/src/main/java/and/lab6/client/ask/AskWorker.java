package and.lab6.client.ask;

//import commands.UpdateID;

import and.lab6.client.utility.Console;
import models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static and.lab6.client.ask.AskCoordinates.askCoordinates;
import static and.lab6.client.ask.AskDate.askDate;
import static and.lab6.client.ask.AskEnum.askEnum;
import static and.lab6.client.ask.AskPerson.askPerson;

public class AskWorker {
    public static Worker askWorker(Console console, boolean scriprtMode) throws AskBreak {
        try {
            String name;
            while (true) {
                console.print("Введите имя name: ");
                name = console.readln().trim();
                ////if (name.equals("exit")) throw new AskBreak();
//                if (name.equals("this") && UpdateID.worker != null) {
//                    name = UpdateID.worker.getName();
//                }
                if (!name.isEmpty()) break;
                else
                    console.print("Имя не может быть пустой строкой!");
                if (scriprtMode) return null;
            }
            //BackUp.println(name)

            Coordinates coordinates = askCoordinates(console, scriprtMode);
            if (scriprtMode && coordinates == null) return null;
            var creationDate = LocalDateTime.now();
            int salary;
            while (true) {
                console.print("Введите зарплату salary: ");
                var line = console.readln().trim();
                if (line.isEmpty())
                    continue;
                if (line.equals(console.getStopWord()) || line.equals(console.getExitWord()))
                    throw new AskBreak(line);
                try {
//                    if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                        salary = UpdateID.worker.getSalary();
//                    } else {
                    salary = Integer.parseInt(line);
                    //}

                    if (salary > 0) break;
                    else {
                        console.print(" Введено неположительное число!");
                        if (scriprtMode) return null;
                    }
                } catch (NumberFormatException e) {
                    console.print("Ошибка! зарпалата - целое положительное число!");
                    if (scriprtMode) return null;
                }
            }
            //BackUp.println(String.valueOf(salary));
            LocalDate endDate = askDate(console, scriprtMode);
            if (scriprtMode && endDate == null) return null;
            Position position = askEnum(Position.class, console, scriprtMode);
            if (scriprtMode && position == null) return null;
            Status status = askEnum(Status.class, console, scriprtMode);
            if (scriprtMode && status == null) return null;
            Person person = askPerson(console, scriprtMode);
            if (scriprtMode && person == null) return null;
            return new Worker(name, coordinates, creationDate, salary, endDate, position, status, person);
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
    }
}
