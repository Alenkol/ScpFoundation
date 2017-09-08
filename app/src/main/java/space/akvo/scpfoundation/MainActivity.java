package space.akvo.scpfoundation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.NumberKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String get_scp_url = "http://scp-wiki-cn.wikidot.com/printer--friendly/scp-";
    final String main_scp_url ="http://scp-wiki-cn.wikidot.com";
    String open_url;
    DataListFragment main_fragment;
    public ShowFragment show_fragment;
    public HubFragment hub_fragment;
    Toolbar toolbar;
    public int state;
    private AccountHeader headerResult;

    public ArrayList<String> back_url = new ArrayList();
    private Drawer result;
    //Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_fragment = new DataListFragment();
        changeFragment(main_fragment);
        //this.savedInstanceState = savedInstanceState;
        headerResult = getAccountHeader(savedInstanceState);
        result = getDrawer(savedInstanceState);
    }

    public void onStart(){
        super.onStart();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.skip:
                SkipScp();
                break;
            default:
                break;
        }
        return true;
    }

    private void SkipScp(){
        final EditText et_skipScp = new EditText(this);
        et_skipScp.setSingleLine();
        et_skipScp.setHint("于此输入Scp号");
        et_skipScp.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0','c','n','-' };
            }
            @Override
            public int getInputType() {
                // TODO Auto-generated method stub
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });

        LinearLayout et_layout = new LinearLayout(this);
        LinearLayout.LayoutParams et_params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et_params.setMargins(20,20,20,15);
        et_skipScp.setLayoutParams(et_params);
        et_layout.addView(et_skipScp);
        new AlertDialog.Builder(this).setTitle("跳转SCP项目").setView(et_layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        final String text_scp = et_skipScp.getText().toString();
                        if (text_scp.length()==0){
                            new AlertDialog.Builder(MainActivity.this).setTitle("警告")
                                    .setMessage("输入项不得为空！")
                                    .setPositiveButton("确定",null).show();
                        }else {
//                            Intent it = new Intent(MainActivity.this,ShowActivity.class);
//                            it.putExtra("url",get_scp_url+text_scp);
//                            startActivity(it);
                            open_url = new String(get_scp_url+text_scp);
                            show_fragment = new ShowFragment();
                            putMessage(show_fragment,"url",open_url);
                            changeFragment(show_fragment);

                        }
                    }
                }).setNegativeButton("取消",null).show();
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void putMessage(Fragment fragment,String key,String value){
        Bundle bundle= new Bundle();
        bundle.putString(key,value);
        fragment.setArguments(bundle);
    }

    public void setState(int i){
        this.state = i;
    }

    public int getState(){
        return this.state;
    }

    public AccountHeader getAccountHeader(Bundle savedInstanceState){
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.bg)
                .withSavedInstance(savedInstanceState)
                .build();
    }

    public Drawer getDrawer(Bundle savedInstanceState){
        return new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withFullscreen(true)
                .addDrawerItems(
                        new ExpandableBadgeDrawerItem().withName("SCP系列档案").withIdentifier(1).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withSubItems(
                                new SecondaryDrawerItem().withName("SCP系列I").withIdentifier(11),
                                new SecondaryDrawerItem().withName("SCP系列II").withIdentifier(12),
                                new SecondaryDrawerItem().withName("SCP系列III").withIdentifier(13),
                                new SecondaryDrawerItem().withName("SCP系列IV").withIdentifier(14),
                                new SecondaryDrawerItem().withName("SCP-CN系列").withIdentifier(15)
                        ),
                        new PrimaryDrawerItem().withName("基金会故事").withIdentifier(2),
                        new PrimaryDrawerItem().withName("设定中心").withIdentifier(3),
                        new PrimaryDrawerItem().withName("关于基金会").withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 11) {
                                changeFragment(main_fragment);
                                main_fragment.setList("select * from Y limit 264,948");
                            } else if(drawerItem.getIdentifier() == 12){
                                changeFragment(main_fragment);
                                main_fragment.setList("select * from Y limit 1212,824");
                            }else if(drawerItem.getIdentifier() == 13){
                                changeFragment(main_fragment);
                                main_fragment.setList("select * from Y limit 2036,433");
                            } else if(drawerItem.getIdentifier() == 14){
                                changeFragment(main_fragment);
                                main_fragment.setList("select * from Y limit 2469,92");
                            }else if(drawerItem.getIdentifier() == 15){
                                changeFragment(main_fragment);
                                main_fragment.setList("select * from Y limit 0,264");
                            }else if (drawerItem.getIdentifier() == 4) {
                                toolbar.setVisibility(View.GONE);
                                changeFragment(new AboutFragment());
                            }else if(drawerItem.getIdentifier() == 3){
                                hub_fragment = new HubFragment();
                                toolbar.setTitle("设定中心");
                                changeFragment(hub_fragment);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

//    public void onBackPressed(){
//        if (!back_url.isEmpty()){
//            show_fragment.set_tz_scp_url(main_scp_url+back_url.get(back_url.size()-1));
//            show_fragment.show_new();
//            back_url.remove(back_url.size()-1);
//        }else{
//            finish();
//        }
//    }

    public ShowFragment getShow_fragment(){
        return new ShowFragment();
    }


//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 1:
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


//    public void setSlide(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                headerResult = getAccountHeader(savedInstanceState);
//                result = getDrawer(savedInstanceState);
//            }
//        }).start();
//    }
}

