package com.mumineendownloads.mumineenpdf.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.marcinorlowski.fonty.Fonty;
import com.mumineendownloads.mumineenpdf.Activities.MainActivity;
import com.mumineendownloads.mumineenpdf.Adapters.FragmentPagerAdapterCustom;
import com.mumineendownloads.mumineenpdf.Adapters.PDFAdapter;
import com.mumineendownloads.mumineenpdf.Adapters.SavedViewPagerAdapter;
import com.mumineendownloads.mumineenpdf.Helpers.CstTabLayout;
import com.mumineendownloads.mumineenpdf.Helpers.PDFHelper;
import com.mumineendownloads.mumineenpdf.Model.PDF;
import com.mumineendownloads.mumineenpdf.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class Saved extends Fragment {
    private MainActivity activity;
    private ArrayList<PDF.PdfBean> arrayList;
    private RecyclerView mRecyclerView;
    private PDFAdapter mPDFAdapter;
    public static ViewPager viewPager;
    private static SavedViewPagerAdapter viewPagerAdapter;
    public static CstTabLayout tabLayout;
    private SearchView searchView;
    private CardView noItemFound;


    public Saved newInstance() {
        return new Saved();
    }

    public Saved() {
    }

    public static Toolbar mActivityActionBarToolbar;


    public Saved(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);
        mActivityActionBarToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mActivityActionBarToolbar);
        mActivityActionBarToolbar.setTitle("Saved PDF");
        Fonty.setFonts(mActivityActionBarToolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        noItemFound = (CardView) rootView.findViewById(R.id.emptyCard);
        ArrayList<String> arrayList;
        PDFHelper pdfHelper = new PDFHelper(getContext());
        arrayList = pdfHelper.getAlbumName();

        viewPagerAdapter = new SavedViewPagerAdapter(getChildFragmentManager(), arrayList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout = (CstTabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if(arrayList.size()==0){
            noItemFound.setVisibility(View.VISIBLE);
            toggleTab(true);
        }
        Fonty.setFonts(tabLayout);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static void toggleTab(boolean hideShow){
        if(hideShow){
            tabLayout.setVisibility(View.GONE);
        }else {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }
}
