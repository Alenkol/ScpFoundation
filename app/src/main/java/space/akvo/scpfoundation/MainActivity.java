package space.akvo.scpfoundation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.NumberKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity{
    final String get_scp_url = "http://scp-wiki-cn.wikidot.com/printer--friendly/scp-";
    final String main_scp_url ="http://scp-wiki-cn.wikidot.com";
    String open_url;
    DataListFragment main_fragment;
    public ShowFragment show_fragment;
    public HubFragment hub_fragment;
    Toolbar toolbar;
    private AccountHeader headerResult;
    public ArrayList<String> back_url = new ArrayList();
    private Drawer result;
    public String toolbarText;
    public AboutFragment aboutFragment;
    private long mPressedTime = 0;
    public int backState;
    public Snackbar snackBar;
    public SwipeRefreshLayout swipeRefresh;
    public int listNum = 0;
    public Fragment nowFragment;
    public StoreBacks sbs;
    public Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sbs = new StoreBacks();
        main_fragment = new DataListFragment();
        main_fragment.toolbarText = "SCP系列I";
        changeFragment(main_fragment);
        nowFragment = main_fragment;
        headerResult = getAccountHeader(savedInstanceState);
        result = getDrawer(savedInstanceState);
    }

    public void onStart(){
        if (global!=null) {
            global.clearList();
        }
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
        if (fragment != main_fragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void putMessage(Fragment fragment,String key,String value){
        Bundle bundle= new Bundle();
        bundle.putString(key,value);
        fragment.setArguments(bundle);
    }

    public void setState(int i){
        this.backState = i;
    }

    public int getState(){
        return this.backState;
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
                        new PrimaryDrawerItem().withName("设定中心").withIdentifier(3),
                        new PrimaryDrawerItem().withName("关于基金会").withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 11) {
                                main_fragment.setListNum(0);
                                main_fragment.toolbarText = "SCP系列I";
                                changeFragment(main_fragment);
                                changeToolbarText("SCP系列I");
                                main_fragment.setList();
                            } else if(drawerItem.getIdentifier() == 12){
                                main_fragment.setListNum(1);
                                main_fragment.toolbarText = "SCP系列II";
                                changeFragment(main_fragment);
                                changeToolbarText("SCP系列II");
                                main_fragment.setList();
                            }else if(drawerItem.getIdentifier() == 13){
                                main_fragment.setListNum(2);
                                main_fragment.toolbarText = "SCP系列III";
                                changeFragment(main_fragment);
                                changeToolbarText("SCP系列III");
                                main_fragment.setList();
                            } else if(drawerItem.getIdentifier() == 14){
                                main_fragment.setListNum(3);
                                main_fragment.toolbarText = "SCP系列IV";
                                changeFragment(main_fragment);
                                changeToolbarText("SCP系列IV");
                                main_fragment.setList();
                            }else if(drawerItem.getIdentifier() == 15){
                                main_fragment.setListNum(4);
                                main_fragment.toolbarText = "SCP-CN系列";
                                changeFragment(main_fragment);
                                changeToolbarText("SCP-CN系列");
                                main_fragment.setList();
                            }else if (drawerItem.getIdentifier() == 4) {
                                aboutFragment = new AboutFragment();
                                aboutFragment.toolbarText = "关于基金会";
                                changeFragment(aboutFragment);
                            }else if(drawerItem.getIdentifier() == 3){
                                hub_fragment = new HubFragment();
                                hub_fragment.toolbarText = "设定中心";
                                changeFragment(hub_fragment);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    public void changeToolbarText(String text){
        toolbar.setTitle(text);
    }
    public ShowFragment getShow_fragment(){
        return new ShowFragment();
    }
    public void showSnackBar(Snackbar sb){
        View v;
        v = sb.getView();
        v.setBackgroundColor(getResources().getColor(R.color.primary));
        ((TextView)v.findViewById(R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.icons));
        ((Button)v.findViewById(R.id.snackbar_action)).setTextColor(getResources().getColor(R.color.icons));
        sb.show();
    }

    public void onBackPressed() {
        if (backState == 0) {
            long mNowTime = System.currentTimeMillis();
            if ((mNowTime - mPressedTime) > 2000) {
                snackBar = Snackbar.make(toolbar,"再按一次退出程序",Snackbar.LENGTH_SHORT);
                showSnackBar(snackBar);
                mPressedTime = mNowTime;
            } else {
                this.finish();
                System.exit(0);
            }
        }else if (backState == 1){
            super.onBackPressed();
        }else if (backState == 2){
            String[] a = sbs.getBack();
            toolbar.setTitle(a[0]);
            show_fragment.setOpenUrl(a[1]);
            show_fragment.getScpDetail();
            sbs.removeBack();
            if (sbs.getBackSize()==1){
                setState(1);
            }
        }
    }
}


