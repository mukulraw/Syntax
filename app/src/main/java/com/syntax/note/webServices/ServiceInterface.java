package com.syntax.note.webServices;




import com.syntax.note.addNoteRequestPOJO.addNoteRequestBean;
import com.syntax.note.addNoteResponsePOJO.addNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.forgotpassRequestPOJO.ForgotpassRequestBean;
import com.syntax.note.forgotpassResponsePOJO.ForgotpassResponseBean;
import com.syntax.note.signinRequestPOJO.signinRequestBean;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.signupRequestPOJO.signupRequestBean;
import com.syntax.note.signupResponsePOJO.signupResponseBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ServiceInterface {

    // method,, return type ,, secondary url
    // ecommerce-android-app-project/new_user_registration.php


  // Signup
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<signupResponseBean>
    signup( @Body signupRequestBean body);

    //SigninforgotpassResponseBean
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<signinResponseBean>
    signin( @Body signinRequestBean body);

    // forgotPassword

  @Headers({"Content-Type: application/json"})
  @POST("api/api.php")
  Call<ForgotpassResponseBean> forgotpassword
          (@Body ForgotpassRequestBean body);

  // categoryList
  @Headers({"Content-Type: application/json"})
  @POST("api/api.php")
  Call<CategoryResponseBean> getCategory
          (@Body CategoryRequestBean body);

  //addNote

  @Headers({"Content-Type: application/json"})
  @POST("api/api.php")
  Call<addNoteResponseBean> addNote
          (@Body addNoteRequestBean body);

}