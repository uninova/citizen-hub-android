package pt.uninova.s4h.citizenhub.report;

public class ResourceType implements LocalizedResource {

    private final String type;

    public ResourceType(String type){
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
