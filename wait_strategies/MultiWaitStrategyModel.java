import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;

import java.util.ArrayList;

/**
 * WaitStrategie mit einer Warteschlange pro Schalter
 */
public class MultiWaitStrategyModel extends WaitStrategyModel {

    private ArrayList<Queue<KundeEntity>> kundenReiheQueues;
    private ArrayList<Queue<SchalterEntity>> freieSchalterQueues;
    private ArrayList<Queue<SchalterEntity>> besetzteSchalterQueues;
    private final int nrSchalter;

    public MultiWaitStrategyModel (Model owner, String name, boolean showInReport, boolean showInTrace, int nrSchalter) {
        super(owner, name, showInReport, showInTrace);
        this.nrSchalter = nrSchalter;
    }

    @Override
    public void init() {
        super.init();

        this.kundenReiheQueues = new ArrayList<>(this.nrSchalter);
        for (int i = 0; i < this.nrSchalter; i++) {
            this.kundenReiheQueues.add(i, new Queue<>(this, "Kunden-Warteschlange_" + (i+1),true, true));
        }

        this.freieSchalterQueues = new ArrayList<>(this.nrSchalter);
        for (int i = 0; i < this.nrSchalter; i++) {
            this.freieSchalterQueues.add(i, new Queue<>(this, "freie Schalter WS_" + (i+1),true, true));
            this.freieSchalterQueues.get(i).insert(new SchalterEntity(this, "Schalter_" + (i+1), true));
        }

        this.besetzteSchalterQueues = new ArrayList<>(this.nrSchalter);
        for (int i = 0; i < this.nrSchalter; i++) {
            this.besetzteSchalterQueues.add(i, new Queue<>(this, "besetzte Schalter WS_" + (i+1),true, true));
        }
    }

    @Override
    public boolean freieSchalterQueueIsEmpty(KundeEntity kunde) {
        return this.freieSchalterQueues.get(kunde.getWarteschlangeZuordnung()).isEmpty();
    }

    @Override
    public void freieSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde) {
        this.freieSchalterQueues.get(kunde.getWarteschlangeZuordnung()).remove(schalter);
    }

    @Override
    public void freieSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde) {
        this.freieSchalterQueues.get(kunde.getWarteschlangeZuordnung()).insert(schalter);
    }

    @Override
    public SchalterEntity freieSchalterQueueFirst(KundeEntity kunde) {
        return  this.freieSchalterQueues.get(kunde.getWarteschlangeZuordnung()).first();
    }

    @Override
    public void besetzteSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde) {
        this.besetzteSchalterQueues.get(kunde.getWarteschlangeZuordnung()).remove(schalter);
    }

    @Override
    public void besetzteSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde) {
        this.besetzteSchalterQueues.get(kunde.getWarteschlangeZuordnung()).insert(schalter);
    }

    @Override
    public SchalterEntity besetzteSchalterQueueFirst(KundeEntity kunde) {
        return this.besetzteSchalterQueues.get(kunde.getWarteschlangeZuordnung()).first();
    }

    @Override
    public boolean kundenWarteschlangeQueueIsEmpty(KundeEntity kunde) {
        return this.kundenReiheQueues.get(kunde.getWarteschlangeZuordnung()).isEmpty();
    }

    @Override
    public void kundenWarteschlangeQueueRemove(KundeEntity kunde) {
        this.kundenReiheQueues.get(kunde.getWarteschlangeZuordnung()).remove(kunde);
    }

    @Override
    public void kundenWarteschlangeQueueInsert(KundeEntity kunde) {
        this.kundenReiheQueues.get(kunde.getWarteschlangeZuordnung()).insert(kunde);
    }

    @Override
    public KundeEntity kundenWarteschlangeQueueFirst(KundeEntity kunde) {
        return this.kundenReiheQueues.get(kunde.getWarteschlangeZuordnung()).first();
    }

    @Override
    public String kundenWarteschlangeQueueToString() {
        StringBuilder s = new StringBuilder("Laenge / avg. Wartezeit der Kundenreihen:" + System.lineSeparator());
        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            int length = this.kundenReiheQueues.get(i).length();
            double avgWait = this.kundenReiheQueues.get(i).averageWaitTime().getTimeAsDouble();
            s.append("  WS_").append(i).append(": ").append(length).append(" / ").append(avgWait).append(System.lineSeparator());
        }
        return s.toString();
    }

    @Override
    public int getShortestWarteschlange() {
        int bestWarteschlangenIndex = 0;
        int bestWarteschlangenLaenge = this.kundenReiheQueues.get(0).length();

        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            int actWarteschlangenLaenge = this.kundenReiheQueues.get(i).length();
            if (bestWarteschlangenLaenge > actWarteschlangenLaenge) {
                bestWarteschlangenLaenge = actWarteschlangenLaenge;
                bestWarteschlangenIndex = i;
            }
        }

        return bestWarteschlangenIndex;
    }

    @Override
    public int getFastestWarteschlange() {
        int bestWarteschlangenIndex = 0;
        double bestWarteschlangenTime = this.kundenReiheQueues.get(0).averageWaitTime().getTimeAsDouble();

        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            if(this.kundenReiheQueues.get(i).length() > 0) {
                double actWarteschlangenTime = this.kundenReiheQueues.get(i).averageWaitTime().getTimeAsDouble();
                if (bestWarteschlangenTime > actWarteschlangenTime) {
                    bestWarteschlangenTime = actWarteschlangenTime;
                    bestWarteschlangenIndex = i;
                }
            } else
                return i;
        }

        return bestWarteschlangenIndex;
    }

    @Override
    public int getBestWarteschlange() {
        int bestWarteschlangenIndex = -1;
        int bestWarteschlangenLaenge = this.kundenReiheQueues.get(0).length();

        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            int actWarteschlangenLaenge = this.kundenReiheQueues.get(i).length();
            if (bestWarteschlangenLaenge > (actWarteschlangenLaenge+1)) {
                bestWarteschlangenLaenge = actWarteschlangenLaenge;
                bestWarteschlangenIndex = i;
            }
        }

        return bestWarteschlangenIndex;
    }

    @Override
    public void doKundenWechsel() {
        int laengeWS1, laengeWS2;
        double avgTimeWS1, avgTimeWS2;
        int bestWarteschlange;

        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            if (this.kundenReiheQueues.get(i).length() > 1) {
                var kunde = this.kundenReiheQueues.get(i).last();

                // beste Warteschlange == k??rzeste Warteschlange
                if (WaitStrategiesExperiments.QUEUE_WECHSEL == 1)
                    bestWarteschlange = this.getBestWarteschlange();
                // beste Warteschlange == schnellste Warteschlange
                else if (WaitStrategiesExperiments.QUEUE_WECHSEL == 2)
                    bestWarteschlange = this.getFastestWarteschlange();
                else
                    throw new RuntimeException("Wrong number declared for global constand WaitStrategiesExperiments.QUEUE_WECHSEL, number "
                            + WaitStrategiesExperiments.QUEUE_WECHSEL + " not allowed (allowed numbers are described above declaration)");

                if (bestWarteschlange != -1 && kunde.getWarteschlangeZuordnung() != bestWarteschlange) {

                    if (WaitStrategiesExperiments.QUEUE_WECHSEL == 1) {
                        laengeWS1 = this.kundenReiheQueues.get(i).length();
                        laengeWS2 = this.kundenReiheQueues.get(bestWarteschlange).length();
                    } else if (WaitStrategiesExperiments.QUEUE_WECHSEL == 2) {
                        avgTimeWS1 = this.kundenReiheQueues.get(i).averageWaitTime().getTimeAsDouble();
                        avgTimeWS2 = this.kundenReiheQueues.get(bestWarteschlange).averageWaitTime().getTimeAsDouble();
                    }

                    kunde.setWarteschlangeZuordnung(bestWarteschlange);
                    this.kundenReiheQueues.get(bestWarteschlange).insert(kunde);
                    this.kundenReiheQueues.get(i).remove(kunde);

                    if(WaitStrategiesExperiments.QUEUE_WECHSEL == 1)
                        sendTraceNote("Kunde wechselt von Warteschlange " + (i + 1) + " (prev. Laenge: " + laengeWS1
                            + ") -> " + (bestWarteschlange + 1) + " (prev. Laenge: " + laengeWS2 + ")");
                    else if (WaitStrategiesExperiments.QUEUE_WECHSEL == 2)
                        sendTraceNote("Kunde wechselt von Warteschlange " + (i + 1) + " (prev. avg. Waittime: " + avgTimeWS1
                                + ") -> " + (bestWarteschlange + 1) + " (prev. avg. Waittime: " + avgTimeWS2 + ")");
                }
            }
        }
    }
}
