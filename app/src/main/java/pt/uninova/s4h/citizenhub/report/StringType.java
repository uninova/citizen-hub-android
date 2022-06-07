package pt.uninova.s4h.citizenhub.report;

public class LabelType implements LocalizedLabel{

    private final String type;

    public LabelType(String type){
        this.type = type;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    @Override
    public String getLocalizedString() {
        return type;
    }

}
