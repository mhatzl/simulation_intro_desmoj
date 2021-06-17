import desmoj.core.simulator.*;

/**
 * Stellt die Erscheinung eines neuen Kunden im System dar
 * -> Beschreibung der Aktionen fuer eine neue Kundenankunft
*/
public class NeuerKundeEvent extends ExternalEvent {

    private final WaitStrategyModel meinModel;

    public NeuerKundeEvent (Model owner, String name, boolean showInTrace) {
	   super(owner, name, showInTrace);

	   this.meinModel = (WaitStrategyModel) owner;
    }

    public void eventRoutine() {
        KundeEntity kunde = new KundeEntity (this.meinModel, "Kunde", true);
        KundenAnkunftEvent kundenAnkunft = new KundenAnkunftEvent (this.meinModel, "Kundenankunft", true);
        kundenAnkunft.schedule(kunde, new TimeSpan(0.0));

        NeuerKundeEvent neuerKunde = new NeuerKundeEvent(this.meinModel, "Kundenkreation", true);
        Long ankunftszeit = this.meinModel.getKundenAnkunftsZeit();
        neuerKunde.schedule (new TimeSpan(ankunftszeit));

        KundenCounter.getInstance().kundenCounterList[(int) meinModel.getExperiment().getSimClock().getTime().getTimeAsDouble()] ++ ;

    }
}
