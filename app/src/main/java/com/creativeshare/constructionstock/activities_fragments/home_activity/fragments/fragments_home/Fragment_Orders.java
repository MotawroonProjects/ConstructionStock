package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.adapters.OrdersAdapter;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Orders extends Fragment {
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private OrdersAdapter adapter;
    private List<OrderDataModel.OrderModel> orderModelList;
    private Preferences preferences;
    private UserModel userModel;
    private Home_Activity activity;
    private boolean isLoading = false;
    private int current_page=1;
    private int selected_pos = -1;

    public static Fragment_Orders newInstance() {

        Fragment_Orders fragment_orders = new Fragment_Orders();
        return fragment_orders;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        orderModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        if (userModel.getUser()==null)
        {
            adapter = new OrdersAdapter(orderModelList,activity,this, Tags.TYPE_USER);
        }else
        {
            adapter = new OrdersAdapter(orderModelList,activity,this, Tags.TYPE_COMPANY);

        }
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_item = adapter.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();

                    if (total_item>5&&last_item_pos==(total_item-5)&&!isLoading)
                    {
                        isLoading = true;
                        orderModelList.add(null);
                        adapter.notifyItemInserted(orderModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);
                    }
                }
            }
        });

        getOrders();


    }



    public void getOrders()
    {
        String user_type;
        int company_id;
        if (userModel.getUser()==null)
        {
            user_type = Tags.TYPE_USER;


        }
        Log.e("user_id",userModel.getUser().getId()+"_");
       /* Api.getService(Tags.base_url)
                .getOrders(user_type,userModel.getUser().getId(),company_id,"1",1)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            orderModelList.clear();
                            orderModelList.addAll(response.body().getData());
                            if (orderModelList.size()>0)
                            {
                                Log.e("2","2");

                                ll_no_order.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                            {
                                Log.e("3","3");

                                ll_no_order.setVisibility(View.VISIBLE);
                            }
                        }else
                        {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });*/
    }

    private void loadMore(int page) {

        String user_type;
        if (userModel.getUser()==null)
        {
            user_type = Tags.TYPE_USER;

        }
      /*  Api.getService(Tags.base_url)
                .getOrders(user_type,userModel.getUser().getId(),userModel.getUser().getCompany_information().getId(),"1",page)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        orderModelList.remove(orderModelList.size()-1);
                        adapter.notifyItemRemoved(orderModelList.size()-1);
                        isLoading = false;
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {

                            orderModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();

                        }else
                        {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });*/
    }
    public void setItemData(OrderDataModel.OrderModel orderModel, int adapterPosition) {
        this.selected_pos =adapterPosition;

         //   activity.DisplayFragmentOrderDetails(orderModel.getId(),orderModel.getOffer_price());

    }

    public void removeItem()
    {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (selected_pos !=-1)
                        {
                            orderModelList.remove(selected_pos);
                            adapter.notifyItemRemoved(selected_pos);
                            selected_pos = -1;

                            if (orderModelList.size()==0)
                            {
                                ll_no_order.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                },1000);

    }
}