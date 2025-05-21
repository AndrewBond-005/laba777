package and.lab6.client.ask;

import and.lab6.client.utility.Console;

import java.util.Arrays;

public class AskEnum {
    public static <T extends Enum<T>> T askEnum(Class<T> param, Console console, boolean scriprtMode) throws AskBreak {
        while (true) {
            console.print("Введите " + "значение" + " " + param.getSimpleName() + Arrays.toString(param.getEnumConstants()) + ": ");
            var line = console.readln().trim();
            if (line.isEmpty())
                continue;
            if (line.equals(console.getStopWord()) || line.equals(console.getExitWord()))
                throw new AskBreak(line);
            if (!line.isEmpty()) {
//                if (line.equalsIgnoreCase("this") && UpdateID.worker != null && UpdateID.worker.getPerson() != null) {
//                    try {
//                        Method method = Worker.class.getMethod("get" + param.getSimpleName());
//                        //BackUp.println(String.valueOf(method.invoke(UpdateID.worker)));
//                        return (T) method.invoke(UpdateID.worker);
//                    } catch (NoSuchMethodException e) {
//                        try {
//                            Method method = Person.class.getMethod("get" + param.getSimpleName());
//                            //BackUp.println(String.valueOf(method.invoke(UpdateID.worker.getPerson())));
//                            return (T) method.invoke(UpdateID.worker.getPerson());
//                        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
//                            console.println("Ошибка! Что-то пошло не так");
//                        }
//                    } catch (InvocationTargetException | IllegalAccessException e) {
//                        console.println("Ошибка! Что-то пошло не так");
//                    }
//                } else
                try {
                    var res = T.valueOf(param, line.toUpperCase());
                    // BackUp.println(String.valueOf(res));
                    return res;
                } catch (IllegalArgumentException e) {
                    console.printError("Ошибка! Введённае значение " + line.toUpperCase() + " не найдено среди значений " + param.getSimpleName());
                    if (console.getFileScanner() != null) {
                        return null;
                    }
                    if (scriprtMode) return null;
                }
            } else return null;
        }
    }

}
