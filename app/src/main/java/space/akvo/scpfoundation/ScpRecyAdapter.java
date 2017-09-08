package space.akvo.scpfoundation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akvo on 2017/9/8.
 */

public class ScpRecyAdapter extends RecyclerView.Adapter<ScpRecyAdapter.ViewHolder> {
    private Context mContext;
    private List<ScpRecyData> mScpRecyList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView tv1;
        TextView tv2;
        TextView tv3;

        public ViewHolder(View view){
            super(view);
            linearLayout = (LinearLayout) view;
            tv1 = (TextView)view.findViewById(R.id.card_tit);
            tv2 = (TextView)view.findViewById(R.id.card_sub);
            tv3 = (TextView)view.findViewById(R.id.card_des);

        }
    }


    public ScpRecyAdapter(List<ScpRecyData> scpRecyList){
        mScpRecyList = scpRecyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScpRecyData scpRecyData = mScpRecyList.get(position);
        holder.tv1.setText(scpRecyData.getTit());
        holder.tv2.setText(scpRecyData.getSub());
        holder.tv3.setText(scpRecyData.getDes());
    }

    @Override
    public int getItemCount(){
        return 24;
    }
}
