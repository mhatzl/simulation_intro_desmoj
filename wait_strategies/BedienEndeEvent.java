import desmoj.core.simulator.*;

/**
 * Stellt das Ende eines Bedienvorgangs am Schalter dar
*/
public class BedienEndeEvent extends Event<KundeEntity> {

    private final WaitStrategyModel meinModel;

    public BedienEndeEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

        this.meinModel = (WaitStrategyModel) owner;
    }

    public void eventRoutine(KundeEntity kunde) {
        // wartet ein weiterer Kunde auf Bedienung?
        if (!this.meinModel.kundenWarteschlangeQueueIsEmpty(kunde)) {
            KundeEntity naechsterKunde = this.meinModel.kundenWarteschlangeQueueFirst(kunde);
            this.meinModel.kundenWarteschlangeQueueRemove(naechsterKunde);

            // Nachdem Warteschlangenlängen sich verändert haben, ist eventuell ein möglicher Wartschlangenwechsel sinnvoll
            this.meinModel.doKundenWechsel();

            BedienEndeEvent bedienEnde = new BedienEndeEvent (this.meinModel, "Bedienung Ende", true);
            bedienEnde.schedule(naechsterKunde, new TimeSpan(this.meinModel.getBedienZeit()));
        }
        else {
            // kein Kunde wartet
            SchalterEntity schalter = this.meinModel.besetzteSchalterQueueFirst(kunde);
            this.meinModel.besetzteSchalterQueueRemove(schalter, kunde);
            this.meinModel.freieSchalterQueueInsert(schalter, kunde);
        }
    }
}
