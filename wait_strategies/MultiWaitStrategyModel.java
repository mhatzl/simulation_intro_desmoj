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
        StringBuilder s = new StringBuilder("Laenge der Kundenreihen:" + System.lineSeparator());
        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            int length = this.kundenReiheQueues.get(i).length();
            s.append("  WS_").append(i).append(": ").append(length).append(System.lineSeparator());
        }
        return s.toString();
    }

    @Override
    public int getBestWarteschlange() {
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
    public void doKundenWechsel() {

        for (int i = 0; i < this.kundenReiheQueues.size(); i++) {
            if (this.kundenReiheQueues.get(i).length() > 1) {
                var kunde = this.kundenReiheQueues.get(i).last();
                var bestWarteschlange = this.getBestWarteschlange();
                if (kunde.getWarteschlangeZuordnung() != bestWarteschlange) {
                    kunde.setWarteschlangeZuordnung(bestWarteschlange);
                    this.kundenReiheQueues.get(bestWarteschlange).insert(kunde);
                    this.kundenReiheQueues.get(i).remove(kunde);

                    sendTraceNote("Kunde wechselt von " + (i + 1) + " -> " + (bestWarteschlange + 1));
                }
            }
        }
    }
}
