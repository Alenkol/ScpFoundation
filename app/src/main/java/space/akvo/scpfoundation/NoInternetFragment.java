package space.akvo.scpfoundation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by akvo on 2017/8/19.
 */

public class NoInternetFragment extends Fragment{

    public String toolbarText;
    private TextView tx_no_inter;
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nointernet,container,false);
        return view;
    }

    public void onStart(){
        super.onStart();
        tx_no_inter = getActivity().findViewById(R.id.noninter);
        tx_no_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity=(MainActivity)getActivity();
                switch (activity.getState()){
                    case 1:
                        activity.putMessage(activity.show_fragment,"url",activity.open_url);
                        activity.changeFragment(activity.show_fragment);
                }
            }
        });
    }
}
