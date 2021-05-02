import desmoj.core.advancedModellingFeatures.Stock;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.extensions.applicationDomains.production.*;

import java.util.concurrent.TimeUnit;

public class ProductionModel extends Model {

    public WorkStation workStation;
    public Processing partsProcessing;
    public ContDistExponential processingTime;
    public PartsList partsList;
    public Entrepot entrepot;
    public RestockProcessQT restockProcess_SubProd_1;
    public RestockProcessQT restockProcess_SubProd_2;
    public RepeatCustomerProcess customerProcess;
    public MainMachineProcess mainMachineProcess;
    public Stock subProduct_1_stock;
    public Stock subProduct_2_stock;
    public ContDistExponential customerArrivalTime;


    public ProductionModel(Model model, String s, boolean b, boolean b1) {
        super(model, s, b, b1);
    }


    @Override
    public String description() {
        return "This model describes a small production site where two parts get combined to one and retrieved by a customerprocess.";
    }

    @Override
    public void doInitialSchedules() {

    }

    @Override
    public void init() {
     //   this.partsList = new PartsList(this, "PartsList", new Class[]{SubProduct_1.class, SubProduct_2.class}, new int[]{1,1}); // needs one of each subproduct
     //   this.workStation = new WorkStation(this, "Workstation", this.partsList, true, true);
        this.entrepot = new Entrepot(this, "Entrepot", true, true);
        this.processingTime = new ContDistExponential(this, "ProcessingTime", 10, true, true);
      //  this.partsProcessing = new Processing(this, "PartsProcessing", this.processingTime, true);
        this.subProduct_1_stock = new Stock(this, "Stock_Subprod_1", 0, 100, true, true); // initialUnits = 0; Capacity = 100
        this.subProduct_2_stock = new Stock(this, "Stock_Subprod_2", 0, 100, true, true);
        this.restockProcess_SubProd_1 = new RestockProcessQT(this, "RestockProcess_Subprod_1", 1, new TimeSpan(5), this.subProduct_1_stock, true);
        this.restockProcess_SubProd_2 = new RestockProcessQT(this, "RestockProcess_Subprod_2", 1, new TimeSpan(5), this.subProduct_2_stock, true);
        this.customerProcess = new RepeatCustomerProcess(this, "CustomerProcess", this.entrepot, 1, true);
        this.customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", 15, true, true);
        this.mainMachineProcess = new MainMachineProcess(this, "MainMachineProcess", true);

        this.restockProcess_SubProd_1.activate();
        this.restockProcess_SubProd_2.activate();
        this.customerProcess.setRepeating(true);
        this.customerProcess.activate();
        this.mainMachineProcess.activate();
    }


    public static void main(String[] args) {
        ProductionModel productionModel = new ProductionModel(null, "ProductionModel", true, true);

        Experiment.setEpsilon(TimeUnit.SECONDS);
        Experiment.setReferenceUnit(TimeUnit.MINUTES);
        Experiment productionExperiment = new Experiment("ProductionExperiment");

        productionModel.connectToExperiment(productionExperiment);

        productionExperiment.stop(new TimeInstant(150, TimeUnit.MINUTES));
        productionExperiment.tracePeriod(new TimeInstant(0), new TimeInstant(150, TimeUnit.MINUTES));
        productionExperiment.debugPeriod(new TimeInstant(0), new TimeInstant(150, TimeUnit.MINUTES));

        productionExperiment.start();
        productionExperiment.report();
        productionExperiment.finish();
    }

}
