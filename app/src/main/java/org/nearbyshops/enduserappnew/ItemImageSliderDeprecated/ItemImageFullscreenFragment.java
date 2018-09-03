package org.nearbyshops.enduserappnew.ItemImageSliderDeprecated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.nearbyshops.enduserappnew.Model.ItemImage;
import org.nearbyshops.enduserappnew.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 23/3/17.
 */

public class ItemImageFullscreenFragment extends Fragment implements ViewPager.OnPageChangeListener {


    ArrayList<ItemImage> dataset = new ArrayList<>();
//    @BindView(R.id.recycler_view)
//    RecyclerView itemImagesList;
//    AdapterItemImages adapterItemImages;
//    GridLayoutManager layoutManager;


    private ViewPager viewPager;

    @BindView(R.id.indicator) TextView indicator;
    @BindView(R.id.caption) TextView caption;
    @BindView(R.id.caption_title) TextView captionTitle;
    @BindView(R.id.copyrights) TextView copyrights;



    public static final String ITEM_IMAGES_INTENT_KEY = "intent_key";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_images_fullscreen, container, false);
        ButterKnife.bind(this,rootView);


        String itemJson = getActivity().getIntent().getStringExtra(ITEM_IMAGES_INTENT_KEY);


        Type listType = new TypeToken<ArrayList<ItemImage>>(){}.getType();




        Gson gson = new Gson();
        dataset = gson.fromJson(itemJson, listType);


        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ImagePagerAdapter(viewPager,dataset,getContext()));
        viewPager.addOnPageChangeListener(this);


        onPageSelected(0);

//        setupRecyclerView();

        return rootView;
    }





//
//    void setupRecyclerView() {

//        adapterItemImages = new AdapterItemImages(dataset,getActivity());
//        itemImagesList.setAdapter(adapterItemImages);

//        LinearSnapHelper snapHelper = new LinearSnapHelper();


//        SnapHelper helper = new LinearSnapHelper();
//        helper.onFling(10,10);
//        helper.attachToRecyclerView(itemImagesList);
//
//        layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
//        itemImagesList.setLayoutManager(layoutManager);
//    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        indicator.setText(String.valueOf(position+1) + " of " + String.valueOf(dataset.size()));
        caption.setText(dataset.get(position).getCaption());
        captionTitle.setText(dataset.get(position).getCaptionTitle());
        copyrights.setText(dataset.get(position).getImageCopyrights());

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
