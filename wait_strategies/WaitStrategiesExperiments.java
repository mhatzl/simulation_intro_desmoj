import desmoj.core.simulator.*;

public class WaitStrategiesExperiments {

    private static void runExperiment(String name, WaitStrategyModel model) {
        Experiment waitStrategyExperiment = new Experiment(name);
        model.connectToExperiment(waitStrategyExperiment);

        waitStrategyExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(60));
        waitStrategyExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(60));

        waitStrategyExperiment.stop(new TimeInstant(240));
        waitStrategyExperiment.start();

        // -> Simulation laeuft bis Abbruchkriterium erreicht ist
        // -> danach geht es hier weiter

        waitStrategyExperiment.report();
        waitStrategyExperiment.finish();
    }

    public static void main(java.lang.String[] args) {

        var singleWaitModel = new SingleWaitStrategyModel(null, "SingleWait Model", true, true, 2);
        runExperiment("WaitStrategie_SingleWS", singleWaitModel);

        var multiWaitStrategyModel = new MultiWaitStrategyModel(null, "MultiWait Model", true, true, 2);
        runExperiment("WaitStrategie_MultiWS", multiWaitStrategyModel);

    }
}
