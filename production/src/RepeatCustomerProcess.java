import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.extensions.applicationDomains.production.CustomerProcess;
import desmoj.extensions.applicationDomains.production.Entrepot;

public class RepeatCustomerProcess extends CustomerProcess {

    ProductionModel model;

    public RepeatCustomerProcess(Model model, String s, Entrepot entrepot, long l, boolean b) {
        super(model, s, entrepot, l, b);
        this.model = (ProductionModel)model;
    }

    @Override
    public void lifeCycle() throws SuspendExecution {
        super.lifeCycle();
        hold(new TimeSpan(this.model.customerArrivalTime.sample()));
    }

}
