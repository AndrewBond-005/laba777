package util;

import models.Worker;

import java.io.Serializable;
import java.util.List;

// из команды
public record Response(
        String message,
        List<Worker> workers,
        int returnCode,
        Action action
) implements Serializable {
    public Response(String message, List<Worker> workers, int returnCode){
        this(message,workers,returnCode,null);
    }
}
