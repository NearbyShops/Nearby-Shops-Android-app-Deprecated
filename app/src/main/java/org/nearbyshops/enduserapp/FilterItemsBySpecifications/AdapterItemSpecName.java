package org.nearbyshops.enduserapp.FilterItemsBySpecifications;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.nearbyshops.enduserapp.ModelItemSpecs.ItemSpecificationName;
import org.nearbyshops.enduserapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterItemSpecName extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemSpecificationName> dataset = null;
    private NotificationsFromAdapterName notifyFragment;
    private Context context;
    private Fragment fragment;

    private int selectedPosition = -1;


    private final static int VIEW_TYPE_PROGRESS_BAR = 2;
    private final static int VIEW_TYPE_NAME = 1;




    AdapterItemSpecName(List<ItemSpecificationName> dataset,
                        NotificationsFromAdapterName notifyFragment,
                        Context context,
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

        if(viewType == VIEW_TYPE_NAME)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_filter_names,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new AdapterItemSpecName.LoadingViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, int position) {

        if(holderGiven instanceof ViewHolder)
        {
            ViewHolder holder = (ViewHolder) holderGiven;

            ItemSpecificationName itemSpecName = dataset.get(position);

            holder.titleName.setText(itemSpecName.getTitle());
//        holder.titleItemSpec.setText(itemSpecName.getTitle());
//        holder.description.setText(itemSpecName.getDescription());


            if(position == selectedPosition)
            {
                holder.titleName.setBackgroundColor(ContextCompat.getColor(context,R.color.blueGrey800));
            }
            else
            {
                holder.titleName.setBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
            }

        }

        else if(holderGiven instanceof AdapterItemSpecName.LoadingViewHolder)
        {

            AdapterItemSpecName.LoadingViewHolder viewHolder = (AdapterItemSpecName.LoadingViewHolder) holderGiven;

            int itemCount = 0;

            if(fragment instanceof FilterItemsFragment)
            {
                itemCount = ((FilterItemsFragment) fragment).item_count_name;
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
//            return VIEW_TYPE_NAME;
//        }

        return VIEW_TYPE_NAME;
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


//        @Bind(R.id.title_item_spec) TextView titleItemSpec;
//        @Bind(R.id.image_item_spec) ImageView imageItemSpec;
//        @Bind(R.id.description) TextView description;

        @BindView(R.id.title_name) TextView titleName;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


//        @OnClick(R.id.more_options)
//        void optionsOverflowClick(View v)
//        {
//            PopupMenu popup = new PopupMenu(context, v);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.item_spec_name_item_overflow, popup.getMenu());
//            popup.setOnMenuItemClickListener(this);
//            popup.show();
//        }



//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//
//            switch (item.getItemId())
//            {
//
//                case R.id.action_remove:
//
////                    showToastMessage("Remove");
//                    notifyFragment.removeItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());
//
//                    break;
//
//                case R.id.action_edit:
//
////                    showToastMessage("Edit");
//                    notifyFragment.editItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());
//
//                    break;
//
//
//                default:
//                    break;
//
//            }
//
//            return false;
//        }


        @Override
        public void onClick(View v) {

            notifyFragment.listItemClick(dataset.get(getLayoutPosition()),getLayoutPosition());

            int previousPosition = selectedPosition;
            selectedPosition = getLayoutPosition();

            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
            
        }
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }





    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    interface NotificationsFromAdapterName{

        void listItemClick(ItemSpecificationName itemSpecName, int position);
    }

}
