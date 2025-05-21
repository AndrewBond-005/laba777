package and.lab6.client.ask;

//import commands.UpdateID;

import and.lab6.client.utility.Console;
import models.Coordinates;

import java.util.NoSuchElementException;

public class AskCoordinates {
    public static Coordinates askCoordinates(Console console, boolean scriprtMode) throws AskBreak {
        try {
            float x;
            while (true) {
                console.print("Введите первую координату coordinates.x: ");
                var line = console.readln().trim();
                if (line.isEmpty())
                    continue;
                if (line.equals(console.getStopWord()) || line.equals(console.getExitWord()))
                    throw new AskBreak(line);
                if (!line.isEmpty()) {
                    try {
//                        if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                            x = UpdateID.worker.getCoordinates().getX();
//                        } else {
                        x = Float.parseFloat(line);
//                        }
                        break;

                    } catch (NumberFormatException e) {
                        console.print("Ошибка! Введено не число!");
                        if (scriprtMode) return null;
                    }
                }
            }
            //BackUp.println(String.valueOf(x));
            Long y;
            while (true) {
                console.print("Введите вторую координату coordinates.y: ");
                var line = console.readln().trim();
                if (line.isEmpty())
                    continue;
                if (line.equals(console.getStopWord()) || line.equals(console.getExitWord()))
                    throw new AskBreak(line);


                if (!line.isEmpty()) {
                    try {
//                        if (line.equalsIgnoreCase("this") && UpdateID.worker != null) {
//                            y = UpdateID.worker.getCoordinates().getY();
//                        } else {
                        y = Long.parseLong(line);
//                        }
                        break;
                    } catch (NumberFormatException e) {
                        console.print("Ошибка! Введено не целое число!");
                        if (scriprtMode) return null;

                    }
                }
            }
            //BackUp.println(String.valueOf(y));
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}
