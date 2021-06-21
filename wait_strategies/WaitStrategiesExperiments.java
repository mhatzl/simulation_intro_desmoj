import desmoj.core.simulator.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WaitStrategiesExperiments {

    /**Warteschlangen-Entscheid bei Kundenankunft (QUEUE_DECISION):
     * true: ein Kunde entscheidet sich bei der Ankunft für die kürzere Warteschlange (realistisches Ereignis)
     * false: ein Kunde entscheidet sich bei der Ankunft für die schnellere Warteschlange (durchschnittliche Wartezeit) oder leere Warteschlange */
    public static final boolean QUEUE_DECISION = false;

    /**Warteschlangen-Wechsel (QUEUE_WECHSEL):
     * 0: ein Kunde darf NICHT die Warteschlange wechseln, wenn er sich bereits für eine Entschieden hat
     * 1: der letzte Kunde einer Warteschlange kann zu einer Warteschlange wechseln die um 2 Kunden KUERZER ist
     * 2: der letzte Kunde einer Warteschlange kann zu einer Warteschlange wechseln die eine KÜRZERE Wartezeit hat */
    public static final int QUEUE_WECHSEL = 0;

    public static final int SIMULATION_TIME = 24 * 60; // h -> min

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

        System.out.println("Single Queue Run");
        var singleWaitModel = new SingleWaitStrategyModel(null, "SingleWait Model", true, true, 2);
        runExperiment("WaitStrategie_SingleWS", singleWaitModel);

        /*
        // Kundenstatistiken zur SingleQueue
        try (PrintWriter writer = new PrintWriter("kundenstatistikData.csv")) {
            StringBuilder sb = new StringBuilder();

            int counter = 0, sum = 0;
            for (int i = 0; i < KundenCounter.getInstance().kundenCounterList.length; i++) {
                if (counter < 10) {
                    counter++;
                    sum += KundenCounter.getInstance().kundenCounterList[i];
                } else {
                    //System.out.println(i + ";" + sum);
                    sb.append(i).append(";").append(sum).append("\n");
                    sum = KundenCounter.getInstance().kundenCounterList[i];
                    counter = 1;
                }
            }
            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        }*/

        System.out.println("Multi Queue Run");
        KundenCounter.resetInstance();

        var multiWaitStrategyModel = new MultiWaitStrategyModel(null, "MultiWait Model", true, true, 2);
        runExperiment("WaitStrategie_MultiWS", multiWaitStrategyModel);

        //Kundenstatistik zur MultiQueue
        try (PrintWriter writer = new PrintWriter("kundenstatistikData.csv")) {
            StringBuilder sb = new StringBuilder();

            int counter = 0, sum = 0;
            for (int i = 0; i < KundenCounter.getInstance().kundenCounterList.length; i++) {
                if (counter < 10) {
                    counter++;
                    sum += KundenCounter.getInstance().kundenCounterList[i];
                } else {
                    //System.out.println(i + ";" + sum);
                    sb.append(i).append(";").append(sum).append("\n");
                    sum = KundenCounter.getInstance().kundenCounterList[i];
                    counter = 1;
                }
            }
            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
