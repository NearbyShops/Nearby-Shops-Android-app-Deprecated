package org.nearbyshops.enduserappnew.FilterItemsBySpecifications;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.nearbyshops.enduserappnew.ModelItemSpecs.ItemSpecificationValue;
import org.nearbyshops.enduserappnew.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterItemSpecValue extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemSpecificationValue> dataset = null;
    private NotificationsFromAdapter notifyFragment;
    private Context context;

    private List<Integer> valuesList = new ArrayList<>();


    private Fragment fragment;


    private final static int VIEW_TYPE_PROGRESS_BAR = 2;
    private final static int VIEW_TYPE_VALUE = 1;


    AdapterItemSpecValue(List<ItemSpecificationValue> dataset,
                         NotificationsFromAdapter notifyFragment, Context context,
                         Fragment fragment)
    {
        this.dataset = dataset;
        this.notifyFragment = notifyFragment;
        this.context = context;

        this.fragment = fragment;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType == VIEW_TYPE_VALUE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_filter_values,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, int position) {

        if(holderGiven instanceof AdapterItemSpecValue.ViewHolder)
        {

            ViewHolder holder = (ViewHolder) holderGiven;

            ItemSpecificationValue itemSpecificationValue = dataset.get(position);

            holder.results.setText("(" + String.valueOf(itemSpecificationValue.getRt_item_count()) + " results)");
            holder.title.setText(itemSpecificationValue.getTitle());


            if(fragment instanceof FilterItemsFragment)
            {
                if(((FilterItemsFragment) fragment).checkboxesList.contains(itemSpecificationValue.getId()))
                {
                    holder.checkBoxValues.setChecked(true);
                }
                else
                {
                    holder.checkBoxValues.setChecked(false);
                }

            }


        }
        else if(holderGiven instanceof LoadingViewHolder)
        {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holderGiven;

            int itemCount = 0;

            if(fragment instanceof FilterItemsFragment)
            {
                itemCount = ((FilterItemsFragment) fragment).item_count_values;
            }


            if(position == 0 || position == itemCount)
            {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);

            }
        }



    }



    @Override
    public int getItemCount() {
        return (dataset.size());
    }



    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

//        if(position==dataset.size())
//        {
//            return VIEW_TYPE_PROGRESS_BAR;
//        }
//        else
//        {
//            return VIEW_TYPE_VALUE;
//        }

        return VIEW_TYPE_VALUE;
    }



    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }









    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


//        @Bind(R.id.title_item_spec) TextView titleItemSpec;
//        @Bind(R.id.image_item_spec) ImageView imageItemSpec;
//        @Bind(R.id.description) TextView description;
//        @Bind(R.id.item_count) TextView itemCount;

        @BindView(R.id.results) TextView results;
        @BindView(R.id.checkbox_values) CheckBox checkBoxValues;
        @BindView(R.id.title_values) TextView title;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            notifyFragment.listItemClick(dataset.get(getLayoutPosition()),getLayoutPosition());
            checkBoxValues.setChecked(!checkBoxValues.isChecked());
            checkChanged();
        }




        @OnClick(R.id.checkbox_values)
        void checkChanged()
        {
            if(checkBoxValues.isChecked())
            {
//                notifyFragment.insertItemSpecItem(dataset.get(getLayoutPosition()).getId());

                valuesList.add(Integer.valueOf(dataset.get(getLayoutPosition()).getId()));

            }
            else if(!checkBoxValues.isChecked())
            {
//                notifyFragment.deleteItemSpecItem(dataset.get(getLayoutPosition()).getId());

                valuesList.remove(Integer.valueOf(dataset.get(getLayoutPosition()).getId()));
            }


        }



    }





    interface NotificationsFromAdapter{

        void listItemClick(ItemSpecificationValue itemSpecValue, int position);
        void deleteItemSpecItem(int itemSpecValueID);
        void insertItemSpecItem(int itemSpecValueID);

    }

}
