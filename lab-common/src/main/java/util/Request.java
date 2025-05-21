package util;

import models.Worker;
import java.util.List;
import java.io.Serializable;

public record Request(
        String command,
        List<String> args,
        List<Worker> workers,
        Action action,
        String login,
        String password
) implements Serializable {

    public Request(String command, List<String> args, List<Worker> workers) {
        this(command, args, workers, null,null,null);
    }
    public Request(String command, List<String> args, List<Worker> workers,Action action) {
        this(command, args, workers, action,null,null);
    }

}