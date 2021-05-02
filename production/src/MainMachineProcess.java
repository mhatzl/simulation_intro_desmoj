import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.extensions.applicationDomains.production.MachineProcess;

public class MainMachineProcess extends MachineProcess {

    private ProductionModel model;

    public MainMachineProcess(Model model, String s, boolean b) {
        super(model, s, b);
        this.model = (ProductionModel) model;
    }

    @Override
    public void lifeCycle() throws SuspendExecution {
        while (true) {
            this.model.subProduct_1_stock.retrieve(1);
            this.model.subProduct_2_stock.retrieve(1);
            //this.model.workStation.process(this.model.partsProcessing);

            this.hold(new TimeSpan(this.model.processingTime.sample()));

            CreatedProduct createdProduct = new CreatedProduct(this.model, "CreatedProduct", false);
            this.model.entrepot.storeProduct(createdProduct);
        }
    }
}
