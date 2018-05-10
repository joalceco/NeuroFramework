package pcell.evaluator;

import org.deeplearning4j.gym.Client;
import org.deeplearning4j.gym.ClientFactory;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.space.ActionSpace;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.GymObservationSpace;
import pcell.model.Model;
import utils.Data;

public class Gym extends Evaluator {

    String problemName = "CartPole-v0";

    public Gym(String problem){
        problemName = problem;
        Client<Box, Integer, DiscreteSpace> client = ClientFactory.build(problemName,true);
        ActionSpace actionSpace = client.getActionSpace();
        GymObservationSpace<Box> observationSpace = client.getObservationSpace();
        String instanceId = client.getInstanceId();
        StepReply<Box> step = client.step(0);
        client.reset();

    }

    @Override
    public double evaluate(Model model) {
        Client<Box, Integer, DiscreteSpace> client = ClientFactory.build("CartPole-v0",true);
        ActionSpace actionSpace = client.getActionSpace();
        GymObservationSpace<Box> observationSpace = client.getObservationSpace();
        String instanceId = client.getInstanceId();
        StepReply<Box> step = client.step(0);
        client.reset();
        return 0;
    }

    @Override
    public double evaluate(Model model, Data X, Data Y) {
        return 0;
    }

    @Override
    public void prepareNextBatch() {

    }
}
