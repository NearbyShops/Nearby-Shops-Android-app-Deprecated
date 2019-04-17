package org.nearbyshops.enduserappnew.SelectMarket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;

public class ViewHolderHeaderMarket extends RecyclerView.ViewHolder {





    public static ViewHolderHeaderMarket create(ViewGroup parent, Context context)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_markets,parent,false);

        return new ViewHolderHeaderMarket(view);
    }




    public ViewHolderHeaderMarket(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }






}
