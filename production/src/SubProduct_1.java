import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class SubProduct_1 extends SimProcess {

    ProductionModel model;

    public SubProduct_1(Model model, String s, boolean b) {
        super(model, s, b);
        this.model = (ProductionModel) model;
    }

    @Override
    public void lifeCycle() throws SuspendExecution {

    }
}
