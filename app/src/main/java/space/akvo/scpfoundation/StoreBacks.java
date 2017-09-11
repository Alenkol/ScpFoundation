package space.akvo.scpfoundation;

import java.util.ArrayList;

/**
 * Created by akvo on 2017/9/11.
 */

public class StoreBacks {
    private ArrayList<String> backUrls;
    private ArrayList<String> backTitles;
    private int size;

    public int getSize(){
        if (backUrls!=null){
            return backUrls.size();
        }else {
            return 0;
        }
    }

    public void addBack(String a,String b){
        if (backUrls==null){
            backUrls = new ArrayList<>();
        }
        if (backTitles==null){
            backTitles = new ArrayList<>();
        }
        backUrls.add(a);
        backTitles.add(b);
    }

    public String[] getBack(){
        size = backUrls.size();
        if (backUrls!=null){
            return new String[]{backUrls.get(size-2),backTitles.get(size-2)};
        }else {
            return null;
        }
    }

    public int getBackSize(){
        return size = backUrls.size();
    }

    public void removeBack(){
        size = backUrls.size();
        if (backUrls!=null){
            backUrls.remove(size-1);
            backTitles.remove(size-1);
        }
    }

    public ArrayList<String> getAllUrls(){
        return backUrls;
    }
    public ArrayList<String> getAllTitles(){
        return backTitles;
    }
}
