package space.akvo.scpfoundation;


import java.util.List;

/**
 * Created by akvo on 2017/9/12.
 */

public class Global {

    private List<String> listItems;

    public List<String> getListItems() {
        return listItems;
    }

    public void setListItems(List<String> listItems) {
        this.listItems = listItems;
    }

    public void clearList(){
        if (listItems!=null) {
            this.listItems = null;
        }
    }
}
