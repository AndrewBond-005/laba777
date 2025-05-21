package and.lab6.client.managers;


import and.lab6.client.commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    Map<String, Command> commands = new HashMap<>();
    private int maxRecursionDeep = 2;

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public int getMaxRecursionDeep() {
        return maxRecursionDeep;
    }

    public void setMaxRecursionDeep(int maxRecursionDeep) {
        this.maxRecursionDeep = maxRecursionDeep;
    }
}
