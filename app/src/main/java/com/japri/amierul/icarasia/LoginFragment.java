package com.japri.amierul.icarasia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends BaseFragment {

    public final static String TAG = "LoginFragment";

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);

        ViewPager viewPager = view.findViewById(R.id.vp_login);
        LoginPagerAdapter adapter = new LoginPagerAdapter(getChildFragmentManager());

        List<String> page = new ArrayList<>();
        page.add(getString(R.string.login));
        page.add(getString(R.string.signup));

        adapter.setPage(page);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tl_login);

        for(String pageTitle : page){
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle));
        }

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
