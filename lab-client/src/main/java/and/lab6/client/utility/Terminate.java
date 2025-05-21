package and.lab6.client.utility;

import and.lab6.client.managers.UDPManager;
import util.Action;

public class Terminate extends Thread {
    private UDPManager udpManager;

    public Terminate(UDPManager udpManager) {
        this.udpManager = udpManager;
    }

    public void run() {
        System.out.println("Завершение программы");
        udpManager.send(Action.CLIENT_DISCONNECTS);
        udpManager.close();
    }
}
