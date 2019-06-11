package org.nearbyshops.enduserappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderHorizontalList extends RecyclerView.ViewHolder {


    @BindView(R.id.recycler_view) RecyclerView recyclerView;



    private Context context;
    private Fragment fragment;




    public static ViewHolderHorizontalList create(ViewGroup parent, Context context, Fragment fragment)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_horizontal_list,parent,false);

        return new ViewHolderHorizontalList(view,context,fragment);
    }





    public ViewHolderHorizontalList(@NonNull View itemView, Context context, Fragment fragment) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }



    public void setItem(RecyclerView.Adapter adapter)
    {
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}
