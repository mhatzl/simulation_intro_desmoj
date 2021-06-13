import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class KundeWechselEvent extends ExternalEvent {

    private final WaitStrategyModel meinModel;

    public KundeWechselEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);

        this.meinModel = (WaitStrategyModel) owner;
    }

    @Override
    public void eventRoutine() {
        this.meinModel.doKundenWechsel();

        KundeWechselEvent kundenWechsel = new KundeWechselEvent (this.meinModel, "Kundenwechsel", true);
        kundenWechsel.schedule(new TimeSpan(this.meinModel.getKundenWechselZeit()));
    }
}
