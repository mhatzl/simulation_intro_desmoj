import desmoj.core.dist.ContDistUniform;
import desmoj.core.dist.DiscreteDistPoisson;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;


public abstract class WaitStrategyModel extends Model {

    private DiscreteDistPoisson kundenAnkunftsZeit;
    private ContDistUniform bedienZeit;
    private final TimeFrame morningPeek = new TimeFrame(6 * 60, 8 * 60); // h -> min
    private final TimeFrame eveningPeek = new TimeFrame(16 * 60, 18 * 60); // h -> min

    private static class TimeFrame {
        private final double startTime;
        private final double endTime;

        TimeFrame(double startTime, double endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        boolean isInTimeFrame(double time) {
            return (this.startTime < time && this.endTime > time);
        }
    }


    /**
     * liefert eine Zufallszahl fuer Kundenankunftszeit
     * Simuliert mehr Kunden zu Peekzeiten
     */
    public Long getKundenAnkunftsZeit() {
        Long ankunftsZeit = kundenAnkunftsZeit.sample();
        double simTime = this.getExperiment().getSimClock().getTime().getTimeAsDouble();

        if (this.morningPeek.isInTimeFrame(simTime) || this.eveningPeek.isInTimeFrame(simTime)) {
            ankunftsZeit /= 2;
        }

        return ankunftsZeit;
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
        this.kundenAnkunftsZeit = new DiscreteDistPoisson(this, "Ankunftszeitintervall", 5, true, true);    // 1 Kunde pro meanValue im Durchschnitt
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
    public abstract int getFastestWarteschlange();
    public abstract int getBestWarteschlange();
    public abstract void doKundenWechsel();

}
