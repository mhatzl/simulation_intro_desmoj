import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.dist.DiscreteDistPoisson;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public abstract class WaitStrategyModel extends Model {

    private DiscreteDistPoisson kundenAnkunftsZeit;
    private ContDistUniform bedienZeit;

    /**
     * liefert eine Zufallszahl fuer Kundenankunftszeit
     */
    public Long getKundenAnkunftsZeit() {
        return kundenAnkunftsZeit.sample();
    }

    /**
     * liefert eine Zufallszahl fuer Bedienzeit
     */
    public double getBedienZeit() {
        return bedienZeit.sample();
    }


    public WaitStrategyModel(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }


    /**
     * Kurzbeschreibung des Modells
     */
    public String description() {
        return "Wait-Strategie Modell (Ereignis orientiert):" +
                "simuliert eine Schalteranwendung, wo ankommende Kunden zuerst in einer oder mehrerer "+
                "Warteschlange(n) eingereiht werden. Wenn ein Schalter der Warteschlange frei ist, "+
                "wird ein Kunde bedient.";
    }

    /**
     * ersten Kunden erzeugen und in Ereignisliste eintragen
     * -> erste Kundenankunft
     */
    public void doInitialSchedules() {
        NeuerKundeEvent ersterKunde = new NeuerKundeEvent(this, "Kundenkreation", true);
        ersterKunde.schedule(new TimeSpan(this.getKundenAnkunftsZeit()));
    }

    /**
     * Initialisierung des Modells
     */
    public void init() {
        this.kundenAnkunftsZeit = new DiscreteDistPoisson(this, "Ankunftszeitintervall", 5, true, true);
        this.kundenAnkunftsZeit.setNonNegative(true);

        this.bedienZeit = new ContDistUniform(this, "Bedienzeiten", 0.5, 10.0, true, true);
    }

    public abstract boolean freieSchalterQueueIsEmpty(KundeEntity kunde);
    public abstract void freieSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde);
    public abstract void freieSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde);
    public abstract SchalterEntity freieSchalterQueueFirst(KundeEntity kunde);

    public abstract void besetzteSchalterQueueRemove(SchalterEntity schalter, KundeEntity kunde);
    public abstract void besetzteSchalterQueueInsert(SchalterEntity schalter, KundeEntity kunde);
    public abstract SchalterEntity besetzteSchalterQueueFirst(KundeEntity kunde);

    public abstract boolean kundenWarteschlangeQueueIsEmpty(KundeEntity kunde);
    public abstract void kundenWarteschlangeQueueRemove(KundeEntity kunde);
    public abstract void kundenWarteschlangeQueueInsert(KundeEntity kunde);
    public abstract KundeEntity kundenWarteschlangeQueueFirst(KundeEntity kunde);
    public abstract String kundenWarteschlangeQueueToString();

    public abstract int getShortestWarteschlange();
    public abstract int getBestWarteschlange();
    public abstract void doKundenWechsel();

}
