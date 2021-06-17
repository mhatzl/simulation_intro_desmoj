import desmoj.core.simulator.*;

/**
 * Stellt das Ereignis einer Kundenankunft dar
*/
public class KundenAnkunftEvent extends Event<KundeEntity> {

    private final WaitStrategyModel meinModel;

    public KundenAnkunftEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

        this.meinModel = (WaitStrategyModel) owner;
    }

    public void eventRoutine(KundeEntity kunde) {
        kunde.setWarteschlangeZuordnung(this.meinModel.getShortestWarteschlange());

        this.meinModel.kundenWarteschlangeQueueInsert(kunde);
        sendTraceNote(this.meinModel.kundenWarteschlangeQueueToString());

        // Schalter frei?
        if (!this.meinModel.freieSchalterQueueIsEmpty(kunde)) {
            SchalterEntity schalter = this.meinModel.freieSchalterQueueFirst(kunde);
            this.meinModel.freieSchalterQueueRemove(schalter, kunde);
            this.meinModel.besetzteSchalterQueueInsert(schalter, kunde);
            this.meinModel.kundenWarteschlangeQueueRemove(kunde);

            BedienEndeEvent bedienEnde =  new BedienEndeEvent (this.meinModel, "Bedienung Ende", true);
            bedienEnde.schedule(kunde, new TimeSpan(this.meinModel.getBedienZeit()));
        }
    }
}
