import desmoj.core.simulator.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WaitStrategiesExperiments {

    /**Warteschlangen-Entscheid bei Kundenankunft (QUEUE_DECISION):
     * true: ein Kunde entscheidet sich bei der Ankunft für die kürzere Warteschlange (realistisches Ereignis)
     * false: ein Kunde entscheidet sich bei der Ankunft für die schnellere Warteschlange (durchschnittliche Wartezeit) oder leere Warteschlange */
    public static final boolean QUEUE_DECISION = true;

    /**Warteschlangen-Wechsel (QUEUE_WECHSEL):
     * 0: ein Kunde darf NICHT die Warteschlange wechseln, wenn er sich bereits für eine Entschieden hat
     * 1: der letzte Kunde einer Warteschlange kann zu einer Warteschlange wechseln die um 2 Kunden KUERZER ist
     * 2: der letzte Kunde einer Warteschlange kann zu einer Warteschlange wechseln die eine KÜRZERE Wartezeit hat */
    public static final int QUEUE_WECHSEL = 0;

    public static final int SIMULATION_TIME = 24 * 60; // h -> min

    // seeds tested:
    // - 9229105
    // - 1229105
    // - 91229105
    // - 591229105
    // - 229105
    // - 123456789
    // - 987654321
    // - 24681012
    // - 135791113
    public static final long KUNDENANKUNFT_SEED = 135791113;

    private static String parametersAsString(int nrSchalter, String wsType){
        StringBuilder sb = new StringBuilder("WaitStrategie_seed_");
        sb.append(KUNDENANKUNFT_SEED);
        sb.append("_Schalter_").append(nrSchalter).append("_Init_");
        if (QUEUE_DECISION){
            sb.append("Length");
        } else {
            sb.append("Time");
        }

        sb.append("_QueueChange_");
        switch (QUEUE_WECHSEL) {
            case 0 -> sb.append("No");
            case 1 -> sb.append("Length");
            case 2 -> sb.append("Time");
        }

        sb.append("_");
        sb.append(wsType);
        return sb.toString();
    }

    private static void runExperiment(String name, WaitStrategyModel model) {
        KundenCounter.resetInstance();

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

        System.out.println("Single Queue Run");
        var singleWaitModel = new SingleWaitStrategyModel(null, "SingleWait Model", true, true, 2);
        runExperiment(parametersAsString(2, "SingleWS"), singleWaitModel);

        singleWaitModel = new SingleWaitStrategyModel(null, "SingleWait Model", true, true, 3);
        runExperiment(parametersAsString(3, "SingleWS"), singleWaitModel);


        System.out.println("Multi Queue Run");

        var multiWaitStrategyModel = new MultiWaitStrategyModel(null, "MultiWait Model", true, true, 2);
        runExperiment(parametersAsString(2, "MultiWS"), multiWaitStrategyModel);

        //Kundenstatistik zur MultiQueue 2
        try (PrintWriter writer = new PrintWriter(parametersAsString(2, "MultiWS_KundenStatistik.csv"))) {
            printCustomerStatistic(writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        multiWaitStrategyModel = new MultiWaitStrategyModel(null, "MultiWait Model", true, true, 3);
        runExperiment(parametersAsString(3, "MultiWS"), multiWaitStrategyModel);

        //Kundenstatistik zur MultiQueue 3
        try (PrintWriter writer = new PrintWriter(parametersAsString(3, "MultiWS_KundenStatistik.csv"))) {
            printCustomerStatistic(writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCustomerStatistic(PrintWriter writer){
        StringBuilder sb = new StringBuilder();

        int counter = 0, sum = 0;
        for (int i = 0; i < KundenCounter.getInstance().kundenCounterList.length; i++) {
            if (counter < 10) {
                counter++;
                sum += KundenCounter.getInstance().kundenCounterList[i];
            } else {
                sb.append(i).append(";").append(sum).append("\n");
                sum = KundenCounter.getInstance().kundenCounterList[i];
                counter = 1;
            }
        }
        writer.write(sb.toString());

        System.out.println("Print customer statistic done!");
    }
}
