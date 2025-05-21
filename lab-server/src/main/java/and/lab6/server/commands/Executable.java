package and.lab6.server.commands;

import util.Request;
import util.Response;

public interface Executable {

    Response execute(Request request);
}