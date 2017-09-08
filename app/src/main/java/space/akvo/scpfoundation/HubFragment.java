package space.akvo.scpfoundation;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akvo on 2017/9/7.
 */

public class HubFragment extends Fragment {
    private List<ScpRecyData> scpRecyList = new ArrayList<>();
    private ScpRecyAdapter adapter;
    MainActivity ma;
    public String toolbarText;
    View view;
    LinearLayout ly;
    LinearLayout ln;
    Context context = getApplication.getInstance();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_hub,container,false);
        return view;
    }

    public void onStart(){
        ma = (MainActivity)getActivity();
        ma.changeToolbarText(toolbarText);
        super.onStart();
        initScpRecyList();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.hub_recycler);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ScpRecyAdapter(scpRecyList);
        rv.setAdapter(adapter);
    }

    private void initScpRecyList(){
        scpRecyList.clear();
        ScpRecyData sd;
        ScpRecyData[] scpRecyDatas = new ScpRecyData[24];
        for (int i =0;i<=23;i++){
            scpRecyDatas[i]  = new ScpRecyData(getResources().getIdentifier("tit"+i,"string",getActivity().getPackageName()),getResources().getIdentifier("des"+i,"string",getActivity().getPackageName()),getResources().getIdentifier("sub"+i,"string",getActivity().getPackageName()));
            //sd = new ScpRecyData(getResources().getIdentifier("tit"+i,"string",getActivity().getPackageName()),getResources().getIdentifier("tit"+i,"string",getActivity().getPackageName()),getResources().getIdentifier("tit"+i,"string",getActivity().getPackageName()));
            scpRecyList.add(scpRecyDatas[i]);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void onStart(){
//        super.onStart();
//        final LayoutInflater inflater = LayoutInflater.from(getApplication.getInstance());
//
//        ly = (LinearLayout)view.findViewById(R.id.hub_in);
//        ma = (MainActivity)getActivity();
//
//        for (int i=1;i<=23;i++) {
//            ln = (LinearLayout) inflater.inflate(
//                    R.layout.card_view, null).findViewById(R.id.hub_lin);
//            ly.addView(ln);
//            TextView tv1 = (TextView) view.findViewById(R.id.card_tit);
//            TextView tv2 = (TextView) view.findViewById(R.id.card_des);
//            TextView tv3 = (TextView) view.findViewById(R.id.card_sub);
//            tv1.setText(getResources().getIdentifier("tit"+i,"string",getActivity().getPackageName()));
//            tv2.setText(getResources().getIdentifier("sub"+i,"string",getActivity().getPackageName()));
//            tv3.setText(getResources().getIdentifier("des"+i,"string",getActivity().getPackageName()));
//            tv1.setId(View.generateViewId());
//            tv2.setId(View.generateViewId());
//            tv3.setId(View.generateViewId());
//        }
//    }
//
//    private void setSets(){
//
//    }
}
