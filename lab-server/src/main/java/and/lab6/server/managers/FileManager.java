package and.lab6.server.managers;

import and.lab6.server.utility.Console;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import models.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

import static models.Worker.fromArray;
import static models.Worker.toArray;


public class FileManager {
    private final String fileName;
    private static final Logger logger = LogManager.getLogger(FileManager.class);


    public FileManager(String fileName, Console console) {
        this.fileName = fileName;
    }


    public void write(Collection<Worker> collection) {
        CSVWriter writer = null;
        File file = new File(fileName);
        if (!file.exists()) {
            logger.warn("Файл не существует");
            try {
                if (file.createNewFile())
                    logger.info("Файл " + fileName + " успешно создан");
            } catch (IOException e) {
                logger.error("ошибка при создании файла!");
            }

        }
        if (!file.canRead()) {
            logger.error("Файл " + fileName + " не досутпен для записи");
            return;
        }
        try {
            var w = new BufferedOutputStream(new FileOutputStream(fileName));//40960
            StringWriter s = new StringWriter();
            writer = new CSVWriter(s);
            int i = 0;
            for (Worker worker : collection) {
                i++;
                writer.writeNext(toArray(worker));
                if (i >= 5000) {
                    //System.out.print(i);
                    i -= 5000;
                    w.write(s.toString().getBytes());
                    w.flush();
                    s.close();
                    writer.close();
                    s = new StringWriter();
                    writer = new CSVWriter(s);
                    w.write(s.toString().getBytes());
                }
            }
            w.write(s.toString().getBytes());
            w.flush();
            s.close();
            w.close();
        } catch (IOException e) {
            logger.error(("Ошибка сериализации:" + e.getCause().getMessage()));
            //return null;
        } catch (NullPointerException e) {
            logger.warn("Файл не найден");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("Ошибка закрытия файла");
            }
        }
    }

    public int read(Collection<Worker> collection) {
        CSVReader reader = null;
        int id = 0;
        File file = new File(fileName);
        if (!file.exists()) {
            logger.warn("Файл не существует");

            try {
                if (file.createNewFile())
                    logger.info("Файл " + fileName + " успешно создан");
            } catch (IOException e) {
                logger.error("ошибка при создании файла!");
            }

        }
        if (!file.canRead()) {
            logger.error("Файл " + fileName + " не доступен для чтения");
            return -1;
        }
        if (file.length() == 0) {
            logger.warn("Файл " + fileName + " пуст!");
            return 0;
        }
        try (Scanner scanner = new Scanner(new FileInputStream(fileName));) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append('\n');
            }
            StringReader stringReader = new StringReader(stringBuilder.toString());
            reader = new CSVReader(stringReader);
            var res = reader.readNext();
            collection.clear();
            HashMap<Integer, Integer> col = new HashMap<>();
            while (res != null) {
                String str;
                Worker worker;
                try {
                    worker = fromArray(res);
                    str = worker.validate();
                    if (str.isEmpty()) {
                        if (!col.containsKey(worker.getId())) {
                            collection.add(worker);
                            id = Math.max(id, worker.getId());
                            col.put(worker.getId(), worker.getId());
                        } else {
                            logger.warn("Worker c id = " + worker.getId() + " уже содержится в коллекции");
                        }
                    } else {
                        logger.warn("Элемент не корректен: " + worker + '\n' +
                                (worker.validate()) + (worker.getPerson().validate()));
                    }
                } catch (NullPointerException e) {
                    logger.warn("Элемент не корректен: id или salary некорректны");
                }
                res = reader.readNext();
            }
            if (!collection.isEmpty()) {
                logger.info("Коллекция успешна загружена!");
                return id;
            }
            stringReader.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            logger.error("Ошибка сериализации:");
            //return null;
            return -1;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("Ошибка закрытия файла");
            }
        }
        return 0;
    }

    public void saveClients(HashSet<InetSocketAddress> sessions) {
        String fileName = "clientAddresses.csv";
        File file = new File(fileName);
        CSVWriter writer = null;
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    logger.info("Файл " + fileName + " успешно создан");
            } catch (IOException e) {
                logger.error("ошибка при создании файла!");
            }
        }
        if (!file.canRead()) {
            logger.error("Файл " + fileName + " не досутпен для записи");
            return;
        }
        try {
            logger.info("запись коллекции в файл");
            var w = new BufferedOutputStream(new FileOutputStream(fileName));
            StringWriter s = new StringWriter();
            writer = new CSVWriter(s);
            for (InetSocketAddress address : sessions) {
                writer.writeNext(String.valueOf(address));
                w.write(s.toString().getBytes());
                w.flush();
                s.close();
                writer.close();
                s = new StringWriter();
                writer = new CSVWriter(s);
                w.write(s.toString().getBytes());
            }
            w.write(s.toString().getBytes());
            w.flush();
            s.close();
            w.close();
        } catch (IOException e) {
            logger.error(("Ошибка сериализации:" + e.getCause().getMessage()));
            //return null;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("Ошибка закрытия файла");
            }
        }

    }

    public HashSet<InetSocketAddress> loadClients() {
        String fileName = "clientAddresses.csv";
        HashSet<InetSocketAddress> clients = new HashSet<>();
        File file = new File(fileName);
        if (!file.exists()) {
            logger.error("Файл не существует");
            try {
                if (file.createNewFile()) {
                    logger.info("Файл " + fileName + " успешно создан");
                }
            } catch (IOException e) {
                logger.error("Ошибка при создании файла!");
            }

            return clients;
        }
        if (!file.canRead()) {
            logger.error("Файл " + fileName + " не доступен для чтения");
            return clients;
        }
        if (file.length() == 0) {
            logger.warn("Файл " + fileName + " пуст!");
            return clients;
        }
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append('\n');
            }
            try (StringReader stringReader = new StringReader(stringBuilder.toString());
                 CSVReader reader = new CSVReader(stringReader)) {
                String[] res;
                while ((res = reader.readNext()) != null) {
                    try {
                        String raw = res[0].trim();
                        if (raw.startsWith("/")) {
                            raw = raw.substring(1); // удаляем слэш
                        }
                        String[] parts = raw.split(":");
                        if (parts.length != 2) continue;
                        InetSocketAddress address = new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
                        clients.add(address);
                    } catch (Exception e) {
                        logger.warn("Неверная строка: " + Arrays.toString(res));
                    }
                }
            }
            if (!clients.isEmpty()) {
                logger.info("Клиенты успешно загружены!");
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла: " + e.getMessage());
        }
        return clients;
    }

}
