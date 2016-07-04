package org.nearbyshops.enduser;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.nearbyshops.enduser.zaDeprecatedItemCategories.ItemCategoriesAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Categories extends AppCompatActivity {



    //@Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.recyclerViewItemCategories) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);

        ItemCategoriesAdapter itemCategoriesAdapter = new ItemCategoriesAdapter(null,this);
        recyclerView.setAdapter(itemCategoriesAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //@OnClick(R.id.fab)
    public void fabClick(View view)
    {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
