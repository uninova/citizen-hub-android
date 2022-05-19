package pt.uninova.s4h.citizenhub.report;

import java.util.List;

public class Group {

    LocalizedLabel label;
    List<Group> group;
    List<Item> item;

    /*
     *
     * */
    public Group(){

    }

    public boolean fillInfo(){
        return true;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    public LocalizedLabel getLocalizedLabel(){
        return label;
    }

    public List<Group> getListGroups() {
        return group;
    }

    public List<Item> getListItems(){
        return item;
    }

}
