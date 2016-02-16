package ru.test.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.test.model.Task;
import ru.test.service.Answer;
import ru.test.service.ITaskSolverService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

// example: http://localhost:8081/rest/sum?uuid=345&arg=1&arg=2
@Controller
@Path("")
public class RestApiEntryPoint extends SpringAwareResource{
    final static Logger logger = LoggerFactory.getLogger(RestApiEntryPoint.class);

    @Autowired
    //@Qualifier("SolverService")
    @Qualifier("SolverByUserService")
    private ITaskSolverService taskSolver;

    @GET @Path("{action}") @Produces(MediaType.APPLICATION_JSON)
    public Answer action(@PathParam("action") String action,
                       @QueryParam("uuid") String uuid,
                       @QueryParam("arg") final List<String> args) {
        logger.debug("Have got task request: action {} uuid {} args {}", action, uuid, String.join(";",args));
        Answer answer = taskSolver.getAnswerFor(uuid, "unknown",action, args);
        logger.debug("Answer is {}", answer);
        return answer;
    }

    @GET @Path("byuser/{action}") @Produces(MediaType.APPLICATION_JSON)
    public Answer actionByUser(@PathParam("action") String action,
                         @QueryParam("uuid") String uuid,
                         @QueryParam("user") String userName,
                         @QueryParam("arg") final List<String> args) {
        logger.debug("Have got task request: user={}, action={} uuid={} args={}", userName, action, uuid, String.join(";",args));
        Answer answer = taskSolver.getAnswerFor(uuid, userName, action, args);
        logger.debug("Answer is {}", answer);
        return answer;
    }

    @GET @Path("getall") @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getAll() {
        return taskSolver.getAllTask();
    }

    @GET @Path("deleteall") @Produces(MediaType.APPLICATION_JSON)
    public String deleteAll() {
        return "Deleted " + taskSolver.deleteAllTask() + " tasks";
    }
}
