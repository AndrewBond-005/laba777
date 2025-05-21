package and.lab6.client.managers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ReceivingManager {
    public Object receive(DatagramChannel channel, int clientPort) {
        try {
            // Получаем датаграмму
            ByteBuffer buffer = ByteBuffer.allocate(65536);
            buffer.clear();
            // System.out.println("Открываем порт, где ждём данные от сервера" + clientPort);
            channel.receive(buffer);
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return ois.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Ошибка при получении данных: " + e.getMessage());
        }
        return null;
    }
}
