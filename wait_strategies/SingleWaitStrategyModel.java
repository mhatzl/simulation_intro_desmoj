import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;

/**
 * WaitStrategie mit einer einzigen Warteschlange
 */
public class SingleWaitStrategyModel extends WaitStrategyModel {

    private Queue<KundeEntity> kundenReiheQueue;
    private Queue<SchalterEntity> freieSchalterQueue;
    private Queue<SchalterEntity> besetzteSchalterQueue;
    private final int nrSchalter;

    public SingleWaitStrategyModel(Model owner, String name, boolean showInReport, boolean showInTrace, int nrSchalter) {
        super(owner, name, showInReport, showInTrace);
        this.nrSchalter = nrSchalter;
    }

    @Override
    public void init() {
        super.init();

        kundenReiheQueue = new Queue<>(this, "Kunden-Warteschlange",true, true);
        freieSchalterQueue = new Queue<>(this, "freie Schalter WS",true, true);

        SchalterEntity schalter;
        for (int i = 1; i <= this.nrSchalter; i++){
            schalter = new SchalterEntity(this, "Schalter_" + i, true);
            freieSchalterQueue.insert(schalter);
        }

        besetzteSchalterQueue = new Queue<>(this, "besetzte Schalter WS", true, true);
    }


    @Override
    public boolean freieSchalterQueueIsEmpty(KundeEntity kunde) {
        return this.freieSchalterQueue.isEmpty();
    }

    @Override
    public void freieSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde) {
        this.freieSchalterQueue.remove(schalter);
    }

    @Override
    public void freieSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde) {
        this.freieSchalterQueue.insert(schalter);
    }

    @Override
    public SchalterEntity freieSchalterQueueFirst(KundeEntity kunde) {
        return this.freieSchalterQueue.first();
    }

    @Override
    public void besetzteSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde) {
        this.besetzteSchalterQueue.remove(schalter);
    }

    @Override
    public void besetzteSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde) {
        this.besetzteSchalterQueue.insert(schalter);
    }

    @Override
    public SchalterEntity besetzteSchalterQueueFirst(KundeEntity kunde) {
        return this.besetzteSchalterQueue.first();
    }

    @Override
    public boolean kundenWarteschlangeQueueIsEmpty(KundeEntity kunde) {
        return this.kundenReiheQueue.isEmpty();
    }

    @Override
    public void kundenWarteschlangeQueueRemove(KundeEntity kunde) {
        this.kundenReiheQueue.remove(kunde);
    }

    @Override
    public void kundenWarteschlangeQueueInsert(KundeEntity kunde) {
        this.kundenReiheQueue.insert(kunde);
    }

    @Override
    public KundeEntity kundenWarteschlangeQueueFirst(KundeEntity kunde) {
        return this.kundenReiheQueue.first();
    }

    @Override
    public String kundenWarteschlangeQueueToString() {
        return "Laenge der Kundenreihe: " + this.kundenReiheQueue.length();
    }

    @Override
    public int getBestWarteschlange() {
        return 0;
    }
}
