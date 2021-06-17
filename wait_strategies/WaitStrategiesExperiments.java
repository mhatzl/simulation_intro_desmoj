import desmoj.core.simulator.*;

public class WaitStrategiesExperiments {

    public static final int SIMULATION_TIME = 1000;

    private static void runExperiment(String name, WaitStrategyModel model) {
        Experiment waitStrategyExperiment = new Experiment(name);
        model.connectToExperiment(waitStrategyExperiment);

        waitStrategyExperiment.tracePeriod(new TimeInstant(0.0), new TimeInstant(SIMULATION_TIME));
        waitStrategyExperiment.debugPeriod(new TimeInstant(0.0), new TimeInstant(SIMULATION_TIME));

        waitStrategyExperiment.stop(new TimeInstant(SIMULATION_TIME));
        waitStrategyExperiment.start();

        // -> Simulation laeuft bis Abbruchkriterium erreicht ist
        // -> danach geht es hier weiter

        waitStrategyExperiment.report();
        waitStrategyExperiment.finish();
    }

    public static void main(java.lang.String[] args) {

        var singleWaitModel = new SingleWaitStrategyModel(null, "SingleWait Model", true, true, 2);
        runExperiment("WaitStrategie_SingleWS", singleWaitModel);

        /*
        for(int i = 0; i < KundenCounter.getInstance().kundenCounterList.length; i++ ){
            if(KundenCounter.getInstance().kundenCounterList[i] != 0)
                System.out.println(i + "," + KundenCounter.getInstance().kundenCounterList[i]);
        }*/

        KundenCounter.resetInstance();

        var multiWaitStrategyModel = new MultiWaitStrategyModel(null, "MultiWait Model", true, true, 2);
        runExperiment("WaitStrategie_MultiWS", multiWaitStrategyModel);

        int counter = 0, sum = 0;
        for(int i = 0; i < KundenCounter.getInstance().kundenCounterList.length; i++ ){
            if(counter < 10) {
                counter++;
                sum += KundenCounter.getInstance().kundenCounterList[i];
            } else {
                System.out.println(i + ";" + sum);
                sum = KundenCounter.getInstance().kundenCounterList[i];
                counter = 1;
            }
        }
    }
}
