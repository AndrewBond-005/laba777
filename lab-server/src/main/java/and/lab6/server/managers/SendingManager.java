package and.lab6.server.managers;

import models.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class SendingManager {
    private int maxWorkerCount = 50;
    private static final Logger logger = LogManager.getLogger(SendingManager.class);

    public void send(int serverPort, InetSocketAddress clientAddress, Object object, DatagramSocket socket) throws Exception {
        if (object instanceof Response) {
            if (((Response) object).workers() != null) {
                // System.out.println(((Response) object).message() + " " + ((Response) object).workers().size());
                if (((Response) object).workers().size() > maxWorkerCount) {
                    logger.info("данных слишком много, надо отправлять несколькими сгементами");
                    sendMany(serverPort, clientAddress, (Response) object, socket, maxWorkerCount);
                    return;
                }
            }
            // System.out.println(((Response) object).message());

        }
        // Сериализация
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        byte[] data = baos.toByteArray();
        // Отправка по UDP

        DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress);
        try {
            logger.info("отправляем на " + clientAddress);
            System.out.println(object);
            socket.send(packet);
            oos.close();
            baos.close();
        } catch (IOException e) {
            logger.info("не получилось, попробуем ещё раз");
            Thread.sleep(150);
            socket.send(packet);
        }
    }

    private void sendMany(int serverPort, InetSocketAddress clientAddress,
                          Response object, DatagramSocket socket, int maxWorkerCount) throws Exception {
        int count = ((object).workers().size() - 1) / maxWorkerCount + 1;
        var message = object.message();
        var returnCode = object.returnCode();
        var workers = object.workers();
        send(serverPort, clientAddress,
                new Response(String.valueOf(count), null, (-1) * count), socket);
        for (int i = 1; i <= count; i++) {
            List<Worker> w = workers.stream()
                    .skip((long) (i - 1) * maxWorkerCount)
                    .limit(maxWorkerCount)
                    .collect(Collectors.toList());
            logger.info("отправляем сгемент номер " + i);
            send(serverPort, clientAddress
                    , new Response(message, w, 0), socket);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
