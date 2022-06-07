package pt.uninova.s4h.citizenhub.report;

public class Item {

    private final LocalizedString label;
    private final LocalizedString value;

    public Item(LocalizedString label, LocalizedString value){
        this.label = label;
        this.value = value;
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

}
