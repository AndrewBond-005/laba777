package and.lab6.server.managers;

import models.Worker;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    private final Set<Worker> tree = Collections.synchronizedSet(
            new TreeSet<>(Comparator.comparing(Worker::getName)));
    private final Map<Integer, Worker> workers = Collections.synchronizedMap(new HashMap<>());
    private int currentId = 1;
    private final LocalDateTime lastInitTime = LocalDateTime.now();
    private LocalDateTime lastSaveTime = null;
    private final DBWorkerManager dbWorkerManager;
    private final DBUserManager dbUserManager;

    public CollectionManager(DBWorkerManager dbWorkerManager, DBUserManager dbUserManager) {
        this.dbWorkerManager = dbWorkerManager;
        this.dbUserManager = dbUserManager;
    }

    public Worker getById(int id) {
        synchronized (workers) {
            return workers.get(id);
        }
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public boolean isContain(Worker e) {
        if (e == null) return true;
        synchronized (workers) {
            return workers.values().stream()
                    .anyMatch(w -> w.getId() == e.getId());
        }
    }

    public boolean update(Worker w, long creatorID) {
        if (w == null) return false;
        if (dbWorkerManager.update(w, creatorID)) {
            synchronized (tree) {
                tree.remove(getById(w.getId()));
                tree.add(w);
            }
            synchronized (workers) {
                workers.put(w.getId(), w);
            }
            return true;
        }
        return false;
    }

    public int getCurrentId() {
        return currentId;
    }

    public int getFreeId() {
        return currentId;
    }

    public boolean add(Worker a, long creatorID) {
        if (dbWorkerManager.insert(a, creatorID)) {
            synchronized (tree) {
                tree.add(a);
            }
            synchronized (workers) {
                workers.put(a.getId(), a);
            }
            return true;
        }
        return false;
    }

    public boolean remove(Worker a, long creatorID) {
        if (a == null) return false;
        if (dbWorkerManager.remove(a.getId(), creatorID)) {
            synchronized (tree) {
                tree.remove(a);
            }
            synchronized (workers) {
                workers.remove(a.getId());
            }
            return true;
        }
        return false;
    }

    public boolean removeAll(long creatorID) {
        List<Integer> list = dbWorkerManager.removeAll(creatorID);
        if (list == null) return false;
        synchronized (tree) {
            synchronized (workers) {
                for (Integer id : list) {
                    tree.remove(workers.get(id));
                    workers.remove(id);
                }
            }
        }
        return true;}

        public Set<Worker> getCollection() {
            return tree;
        }

        public boolean loadCollection() {
            List<Worker> worker = dbWorkerManager.load();
            if (worker != null) {
                synchronized (tree) {
                    tree.clear();
                    tree.addAll(worker);
                }
                synchronized (workers) {
                    workers.clear();
                    workers.putAll(tree.stream()
                            .collect(Collectors.toMap(Worker::getId, w -> w)));
                }
                int id = dbWorkerManager.maxID();
                currentId = id > 0 ? id + 1 : 1;
                return id > 0;
            }
            currentId = 1;
            return true;
        }

        public void saveCollection() {
            lastSaveTime = LocalDateTime.now();
        }

        public long findUser(String login, String password) {
            return dbUserManager.verifyUser(login, password);
        }
    }