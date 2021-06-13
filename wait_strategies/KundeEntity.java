import desmoj.core.simulator.*;

/**
 * Zur Darstellung von Kunden
 * -> einfache Version, keine speziellen Attribute notwendig
*/
public class KundeEntity extends Entity {

    private int warteschlangeZuordnung = 0;

    public KundeEntity(Model owner, String name, boolean showInTrace) {
	   super(owner, name, showInTrace);
    }


    public void setWarteschlangeZuordnung(int nrWarteschlange) {
        this.warteschlangeZuordnung = nrWarteschlange;
    }

    public int getWarteschlangeZuordnung() {
        return this.warteschlangeZuordnung;
    }
}
