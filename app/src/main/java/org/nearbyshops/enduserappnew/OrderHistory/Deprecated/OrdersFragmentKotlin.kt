package org.nearbyshops.enduserappnew.OrderHistory.Deprecated

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.wunderlist.slidinglayer.SlidingLayer
import okhttp3.ResponseBody
import org.nearbyshops.enduserappnew.API.OrderService
import org.nearbyshops.enduserappnew.DaggerComponentBuilder
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch
import org.nearbyshops.enduserappnew.Interfaces.NotifySort
import org.nearbyshops.enduserappnew.Interfaces.RefreshFragment
import org.nearbyshops.enduserappnew.Login.Login
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Endpoints.OrderEndPoint
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order
import org.nearbyshops.enduserappnew.OrderDetail.OrderDetail
import org.nearbyshops.enduserappnew.OrderDetail.PrefOrderDetail
import org.nearbyshops.enduserappnew.OrderHistory.OrderHistory
import org.nearbyshops.enduserappnew.OrderHistory.OrdersHistoryFragment
import org.nearbyshops.enduserappnew.OrderHistory.SlidingLayerSort.PrefSortOrders
import org.nearbyshops.enduserappnew.OrderHistory.SlidingLayerSort.SlidingLayerSortOrders
import org.nearbyshops.enduserappnew.Preferences.PrefLogin
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome
import org.nearbyshops.enduserappnew.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import javax.inject.Inject
import java.util.ArrayList



class OrdersFragmentKotlin : Fragment(), Adapter.NotifyConfirmOrder, SwipeRefreshLayout.OnRefreshListener,
    NotifySort, NotifySearch, RefreshFragment {

    @Inject lateinit var orderService: OrderService

    internal var recyclerView: RecyclerView? = null
    internal var adapter: Adapter? = null

    var dataset: MutableList<Order> = ArrayList()


    internal var layoutManager: GridLayoutManager? = null
    internal var swipeContainer: SwipeRefreshLayout? = null


    private val limit = 10
    internal var offset = 0
    internal var item_count = 0

    internal var isDestroyed: Boolean = false


    @BindView(R.id.slidingLayer) var slidingLayer: SlidingLayer? = null
    @BindView(R.id.shop_count_indicator) var orderCountIndicator: TextView? = null



    @BindView(R.id.empty_screen) var emptyScreen: LinearLayout? = null


    internal var searchQuery: String? = null



    init {

        DaggerComponentBuilder.getInstance()
            .netComponent
            .Inject(this)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        retainInstance = true
        val rootView = inflater.inflate(R.layout.fragment_orders_new, container, false)
        ButterKnife.bind(this, rootView)


        recyclerView = rootView.findViewById<View>(R.id.recyclerView) as RecyclerView
        swipeContainer = rootView.findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout


        if (savedInstanceState == null) {
            makeRefreshNetworkCall()
        }


        val toolbar = rootView.findViewById<View>(R.id.toolbar) as Toolbar
        //        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        //        toolbar.setTitle(getString(R.string.app_name));
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        //


        setupRecyclerView()
        setupSwipeContainer()

        setupSlidingLayer()


        return rootView
    }


    internal fun setupSwipeContainer() {
        if (swipeContainer != null) {

            swipeContainer!!.setOnRefreshListener(this)
            swipeContainer!!.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
        }

    }


    internal fun setupSlidingLayer() {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if (slidingLayer != null) {
            slidingLayer!!.setChangeStateOnTap(true)
            slidingLayer!!.isSlidingEnabled = true
            slidingLayer!!.setPreviewOffsetDistance(15)
            slidingLayer!!.offsetDistance = 10
            slidingLayer!!.setStickTo(SlidingLayer.STICK_TO_RIGHT)

            //            DisplayMetrics metrics = new DisplayMetrics();
            //            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if (childFragmentManager.findFragmentByTag(TAG_SLIDING_LAYER) == null) {
                childFragmentManager
                    .beginTransaction()
                    .add(R.id.slidinglayerfragment, SlidingLayerSortOrders(),
                        TAG_SLIDING_LAYER
                    )
                    .commit()
            }

        }
    }


    @OnClick(R.id.icon_sort, R.id.text_sort)
    internal fun sortClick() {
        slidingLayer!!.openLayer(true)
    }


    internal fun setupRecyclerView() {

        adapter = Adapter(dataset, this, this, activity)

        recyclerView?.adapter = adapter

        layoutManager = GridLayoutManager(activity, 1)
        recyclerView?.layoutManager = layoutManager

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        //        layoutManager.setSpanCount(metrics.widthPixels/400);


        var spanCount = (metrics.widthPixels / (230 * metrics.density)).toInt()

        if (spanCount == 0) {
            spanCount = 1
        }


        layoutManager!!.spanCount = spanCount


        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)



                if (offset + limit > layoutManager!!.findLastVisibleItemPosition() + 1 - 1) {
                    return
                }



                if (layoutManager!!.findLastVisibleItemPosition() == dataset.size - 1 + 1) {
                    // trigger fetch next page

                    //                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
                    //                    {
                    //                        return;
                    //                    }


                    if (offset + limit <= item_count) {
                        offset = offset + limit
                        makeNetworkCall(false)
                    }

                    //                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        })
    }


    //    int previous_position = -1;


    override fun onRefresh() {

        offset = 0
        makeNetworkCall(true)
    }


    internal fun makeRefreshNetworkCall() {

        swipeContainer!!.post {
            swipeContainer!!.isRefreshing = true

            onRefresh()
        }


    }


    internal fun makeNetworkCall(clearDataset: Boolean) {

        //            Shop currentShop = UtilityShopHome.getShop(getContext());



        val endUser = PrefLogin.getUser(activity)

        if (endUser == null) {
            showLoginDialog()

            swipeContainer?.isRefreshing = false
            return
        }





        var current_sort = ""
        current_sort = PrefSortOrders.getSort(activity) + " " + PrefSortOrders.getAscending(activity)

        var shopID: Int? = null

        if (activity!!.intent.getBooleanExtra(OrderHistory.IS_FILTER_BY_SHOP, false)) {
            val shop = PrefShopHome.getShop(activity!!)

            if (shop != null) {
                shopID = shop.shopID
            } else {
                shopID = 0
            }
        }


        var pickFromShop: Boolean? = null

        if (PrefSortOrders.getFilterByDeliveryType(activity!!) == SlidingLayerSortOrders.FILTER_BY_PICK_FROM_SHOP) {
            pickFromShop = true
        } else if (PrefSortOrders.getFilterByDeliveryType(activity!!) == SlidingLayerSortOrders.FILTER_BY_HOME_DELIVERY) {
            pickFromShop = false
        }


        var ordersPendingStatus: Boolean? = null

        if (PrefSortOrders.getFilterByOrderStatus(activity!!) == SlidingLayerSortOrders.FILTER_BY_STATUS_PENDING) {
            ordersPendingStatus = true
        } else if (PrefSortOrders.getFilterByOrderStatus(activity!!) == SlidingLayerSortOrders.FILTER_BY_STATUS_COMPLETE) {
            ordersPendingStatus = false
        }




        emptyScreen?.visibility = View.GONE


        val call = orderService.getOrders(
            PrefLogin.getAuthorizationHeaders(activity), null, shopID,
            true,
            pickFromShop, null, null, null, null, null,
            ordersPendingStatus, searchQuery,
            current_sort, limit, offset, null
        )



        call.enqueue(object : Callback<OrderEndPoint> {
            override fun onResponse(call: Call<OrderEndPoint>, response: Response<OrderEndPoint>) {

                if (isDestroyed) {
                    return
                }


                if (response.code() == 200) {
                    if (response.body() != null) {
                        item_count = response.body()!!.itemCount!!

                        if (clearDataset) {
                            dataset.clear()
                        }

                        if (response.body()!!.results != null) {
                            dataset.addAll(response.body()!!.results)

                            if (response.body()!!.results.size == 0) {
                                emptyScreen!!.visibility = View.VISIBLE
                            }
                        }


                        adapter?.notifyDataSetChanged()
                        //                            notifyTitleChanged();


                        orderCountIndicator?.text =
                            dataset.size.toString() + " out of " + item_count.toString() + " Orders"
                    }

                } else {

                    showToastMessage("Failed Code : " + response.code().toString())

                }


                swipeContainer!!.isRefreshing = false
            }


            override fun onFailure(call: Call<OrderEndPoint>, t: Throwable) {

                if (isDestroyed) {
                    return
                }


                emptyScreen!!.visibility = View.VISIBLE



                showToastMessage("Network Request failed !")
                swipeContainer!!.isRefreshing = false

            }
        })

    }


    @OnClick(R.id.button_try_again)
    internal fun tryAgainClick() {
        makeRefreshNetworkCall()
    }


    override fun onResume() {
        super.onResume()
        //        notifyTitleChanged();
        isDestroyed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
    }


    internal fun showToastMessage(message: String) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

    }


    //    void refreshConfirmedFragment()
    //    {
    //        Fragment fragment = getActivity().getSupportFragmentManager()
    //                .findFragmentByTag(makeFragmentName(R.id.container,1));
    //
    //        if(fragment instanceof RefreshFragment)
    //        {
    //            ((RefreshFragment)fragment).refreshFragment();
    //        }
    //    }


    override fun notifyOrderSelected(order: Order) {
        PrefOrderDetail.saveOrder(order, activity!!)
        activity!!.startActivity(Intent(activity, OrderDetail::class.java))
    }


    override fun notifyCancelOrder(order: Order) {


        val builder = AlertDialog.Builder(activity!!)

        builder.setTitle("Confirm Cancel Order !")
            .setMessage("Are you sure you want to cancel this order !")
            .setPositiveButton("Yes") { dialog, which -> cancelOrder(order) }
            .setNegativeButton(
                "No"
            ) { dialog, which -> showToastMessage(" Not Cancelled !") }
            .show()
    }


    private fun cancelOrder(order: Order) {

        showToastMessage("Cancel Order !")


        //        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());

        val call = orderService.cancelledByEndUser(
            PrefLogin.getAuthorizationHeaders(activity),
            order.orderID
        )


        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 200) {
                    showToastMessage("Successful")
                    makeRefreshNetworkCall()
                } else if (response.code() == 304) {
                    showToastMessage("Not Cancelled !")
                } else {
                    showToastMessage("Server Error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                showToastMessage("Network Request Failed. Check your internet connection !")
            }
        })

    }


    override fun notifySortChanged() {
        makeRefreshNetworkCall()
    }

    override fun search(searchString: String) {
        searchQuery = searchString
        makeRefreshNetworkCall()
    }

    override fun endSearchMode() {
        searchQuery = null
        makeRefreshNetworkCall()
    }

    private fun showLoginDialog() {

        //        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG);
        //
        //        if(getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG)==null)
        //        {
        //            FragmentManager fm = getActivity().getSupportFragmentManager();
        //            LoginDialog loginDialog = new LoginDialog();
        //            loginDialog.show(fm,TAG_LOGIN_DIALOG);
        //        }


        val intent = Intent(activity, Login::class.java)
        startActivity(intent)
    }


    override fun refreshFragment() {
        makeRefreshNetworkCall()
    }

    companion object {


        //    @Inject
        //    OrderServicePFS orderService;

        val TAG_SLIDING_LAYER = "sliding_layer_orders"
        val IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP"


        fun newInstance(): OrdersHistoryFragment {
            val fragment = OrdersHistoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }


        //
        //    void notifyTitleChanged()
        //    {
        //
        //        if(getActivity() instanceof NotifyTitleChanged)
        //        {
        //            ((NotifyTitleChanged)getActivity())
        //                    .NotifyTitleChanged(
        //                            "Pending (" + String.valueOf(dataset.size())
        //                                    + "/" + String.valueOf(item_count) + ")",0);
        //
        //
        //        }
        //    }


        // Refresh the Confirmed PlaceholderFragment

        private fun makeFragmentName(viewId: Int, index: Int): String {
            return "android:switcher:$viewId:$index"
        }


        val TAG_LOGIN_DIALOG = "tag_login_dialog"
    }

}
