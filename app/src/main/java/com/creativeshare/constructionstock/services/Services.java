package com.creativeshare.constructionstock.services;




import com.creativeshare.constructionstock.models.TermsModel;
import com.creativeshare.constructionstock.models.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;



public interface Services {

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> sign_up(
            @Field("username") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("software_type") int software_type
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> sign_in(
            @Field("email") String email,
            @Field("password") String password

    );


    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> contact_us(@Field("username") String name,
                                  @Field("email") String email,
                                  @Field("message") String message
    );

    @GET("api/terms-and-conditions")
    Call<TermsModel> getTerms();


}