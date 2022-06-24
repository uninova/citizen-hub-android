package pt.uninova.s4h.citizenhub.report;

public class Item {

    private final LocalizedString label;
    private final LocalizedString value;
    private final LocalizedString units;

    public Item(LocalizedString label, LocalizedString value, LocalizedString units){
        this.label = label;
        this.value = value;
        this.units = units;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    public LocalizedString getLabel(){
        return label;
    }

    public LocalizedString getValue(){
        return value;
    }

    public LocalizedString getUnits() { return units; }

}
