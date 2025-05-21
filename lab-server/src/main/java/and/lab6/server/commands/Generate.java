package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.*;
import util.Request;
import util.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;


public class Generate extends Command {
    private final CollectionManager collectionManager;

    public Generate(CollectionManager collectionManager) {
        super("generate {count}", "добавить новых элементов в коллекцию");
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Не введён аругемнт", null,  449);
        }
        int count = (int) (1000 * 1000);
        try {
            count = Integer.parseInt(request.args().get(0).trim());
        } catch (Exception e) {
            return new Response("Целое число не распознано",
                    null,  448);
        }
        long id=  collectionManager.findUser(request.login(), request.password());
        IntStream.rangeClosed(1, count)
                .mapToObj(i -> generateWorker())
                .forEach(worker -> collectionManager.add(worker, id));
        return new Response("Worker успешно добавлен", null, 200);
    }


        Worker generateWorker(){
            Random random = new Random();
            int rn = abs(abs(random.nextInt()) + 666);
            //System.out.println(rn);
            int letters = (rn % 7) + 3;
            StringBuilder name = new StringBuilder();
            name.append((char) ((rn % 26) + 65));
            for (int j = 1; j <= letters - 1; j++) {
                name.append((char) (random.nextInt(26) + 97));
            }
            int id = collectionManager.getFreeId();
            int salary = (rn % 67) * (rn % 83) + 1;
            float x = (float) (((rn % 53 + 1) * (rn % 101) * (rn % 43 + 1) + 1) * (0.0001) * ((rn % 7) % 2 == 0 ? 1 : -1));
            Long y = (long) ((rn % 83 + 1) * (rn % 73 + 1) * ((rn % 11) % 2 == 0 ? 1 : -1));
            Coordinates coordinates = new Coordinates(x, y);
            var creationDate = LocalDateTime.now();
            LocalDate endDate = LocalDate.ofEpochDay(creationDate.toLocalDate().toEpochDay() + (rn % 47) * (rn % 97));
            Position position = null;
            Status status = null;
            for (Position pos : Position.values()) {
                if (pos.ordinal() == rn % 5) {
                    position = pos;
                    break;
                }
            }
            for (Status stat : Status.values()) {
                if (stat.ordinal() == rn % 3) {
                    status = stat;
                    break;
                }
            }
            Person person = generatePerson();
            return new Worker(id, name.toString(), coordinates, creationDate, salary, endDate, position, status, person);
        }

        private static Person generatePerson () {
            Random random = new Random();
            var rn = (abs(abs(random.nextInt()) + 666));
            Double weight = (float) ((rn % 53) * (rn % 43) * (rn % 101) + 1) * (0.0001); //Поле не может быть null, Значение поля должно быть больше 0
            EyeColor eyeColor = null; //Поле может быть null
            HairColor hairColor = null; //Поле может быть null
            Country nationality = null; //Поле не может быть null
            for (EyeColor eyeClr : EyeColor.values()) {
                if (eyeClr.ordinal() == rn % 5) {
                    eyeColor = eyeClr;
                    break;
                }
            }
            rn = (abs(abs(random.nextInt()) + 666));
            for (HairColor hairClr : HairColor.values()) {
                if (hairClr.ordinal() == rn % 5) {
                    hairColor = hairClr;
                    break;
                }
            }
            rn = (abs(abs(random.nextInt()) + 666));
            for (Country national : Country.values()) {
                if (national.ordinal() == rn % 5) {
                    nationality = national;
                    break;
                }
            }
            return new Person(weight, eyeColor, hairColor, nationality);
        }
    }
//private Double weight; //Поле не может быть null, Значение поля должно быть больше 0
//private EyeColor eyeColor; //Поле может быть null
//private HairColor hairColor; //Поле может быть null
//private Country nationality; //Поле не может быть null


//private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
//private String name; //Поле не может быть null, Строка не может быть пустой
//private Coordinates coordinates; //Поле не может быть null
//private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
//private int salary; //Значение поля должно быть больше 0
//private LocalDate endDate; //Поле может быть null
//private Position position; //Поле не может быть null
//private Status status; //Поле не может быть null
//private Person person; //Поле не может быть null