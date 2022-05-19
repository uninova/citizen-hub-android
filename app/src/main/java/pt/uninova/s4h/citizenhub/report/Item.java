package pt.uninova.s4h.citizenhub.report;

public class Item {

    private LocalizedLabel label;
    private final int value;

    private Item(int value){
        this.value = value;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    public String getString(){
        return getLabel().getLocalizedString();
    }

    public LocalizedLabel getLabel(){
        return label;
    }

    public int getValue(){
        return value;
    }

}
