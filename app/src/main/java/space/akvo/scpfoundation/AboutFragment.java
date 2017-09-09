package space.akvo.scpfoundation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by akvo on 2017/9/8.
 */

public class AboutFragment extends Fragment {

    public String toolbarText;
    private MainActivity ma;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        return view;
    }
    public void onStart(){
        super.onStart();
        ma = (MainActivity)getActivity();
        ma.backState = 0;
    }
}
