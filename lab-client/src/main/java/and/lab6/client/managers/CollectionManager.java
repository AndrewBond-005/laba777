package and.lab6.client.managers;

import models.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionManager {
    private List<Worker> workersList = new ArrayList<>();
    private final Map<Integer, Worker> workersMap = new HashMap<>();
    private int lastWorkerId;

    public Worker getById(int id) {
        return workersMap.get(id);
    }

    public int getLastWorker() {
        return lastWorkerId;
    }

    public boolean add(Worker worker) {
        workersMap.put(worker.getId(), worker);
        workersList.add(worker);
        return true;
    }

    public void removeAll() {
        workersList.clear();
        workersMap.clear();
    }

    public List<Worker> getCollection() {
        return (workersList).isEmpty() ? new ArrayList<>() : workersList;
    }

    public void setLastWorker(int id) {
        this.lastWorkerId = id;
    }

}