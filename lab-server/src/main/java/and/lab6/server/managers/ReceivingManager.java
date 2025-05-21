package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

public class ReceivingManager {
    private static final Logger logger = LogManager.getLogger(ReceivingManager.class);
    public InetSocketAddress lastReceivedAddress;

    public Object receive(int port, DatagramSocket socket) {
        try {
            byte[] buffer = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            this.lastReceivedAddress = (InetSocketAddress) packet.getSocketAddress();
            return ois.readObject();
        } catch (SocketException e) {
            if (socket.isClosed()) {
                logger.info("Сокет закрыт, завершаем прием");
                return null;
            }
            logger.error(e);
            return null;
        } catch (StreamCorruptedException e) {
            logger.warn("Получен некорректный поток данных:"+ e.getMessage());
            return null;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка при получении данных");
            return null;
        }
    }
}