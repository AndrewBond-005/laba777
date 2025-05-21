package and.lab6.client.managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UDPManager {
    ReceivingManager receivingManager;
    SendingManager sendingManager = new SendingManager();
    private int serverPort = 46525;
    private int clientPort = 4652;
    private DatagramChannel channel;
    private Selector selector;
    private int maxWorkerCount = 490;
    private UserManager userManager;

    public int getMaxWorkerCount() {
        return maxWorkerCount;
    }

    public UDPManager(ReceivingManager receivingManager, SendingManager sendingManager,
                      int serverPort, int clientPort,UserManager userManager ) {
        this.receivingManager = receivingManager;
        this.sendingManager = sendingManager;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.userManager = userManager;
        try {
            channel = DatagramChannel.open();
            // Привязываем DatagramChannel к указанному порту
            channel.bind(new InetSocketAddress(clientPort));
            channel.configureBlocking(false);

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("Не удалось создать сетевой канал");
        }


    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void send(Object object) {
        sendingManager.send(object, serverPort, clientPort, channel);

    }

    public Object receive(long timeoutMillis) {
        try {
            if (!selector.isOpen()) {
                System.exit(0);
            }
            if (selector.select(timeoutMillis) > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isReadable()) {
                        return receivingManager.receive(channel, clientPort);
                    }
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Error receiving data" + e);
        }
        return null;
    }

    public void close() {
        try {
            if (selector != null) {
                selector.close();
            }
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
