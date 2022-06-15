package pt.uninova.s4h.citizenhub.report;

import java.util.LinkedList;
import java.util.List;

public class Group {

    private final LocalizedString label;
    private final List<Group> groupList;
    private final List<Item> itemList;

    /*
     *
     * */
    public Group(LocalizedString label){
        this.label = label;
        this.groupList = new LinkedList<>();
        this.itemList = new LinkedList<>();
    }

    /***************************************
1     * This section only has get functions *
     ***************************************/
    public LocalizedString getLabel(){
        return label;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public List<Item> getItemList(){
        return itemList;
    }

}
