package com.syntax.note.webServices;


import com.syntax.note.addNoteRequestPOJO.addNoteRequestBean;
import com.syntax.note.addNoteResponsePOJO.addNoteResponseBean;
import com.syntax.note.allNoteResponsePOJO.allNoteResponseBean;
import com.syntax.note.categoryRequestPOJO.CategoryRequestBean;
import com.syntax.note.categoryResponsePOJO.CategoryResponseBean;
import com.syntax.note.deleteNoteRequestPOJO.deleteNoteRequestBean;
import com.syntax.note.forgotpassRequestPOJO.ForgotpassRequestBean;
import com.syntax.note.forgotpassResponsePOJO.ForgotpassResponseBean;
import com.syntax.note.searchRequestPOJO.searchRequestBean;
import com.syntax.note.searchResultPOJO.searchResultBean;
import com.syntax.note.signinRequestPOJO.signinRequestBean;
import com.syntax.note.signinResponsePOJO.signinResponseBean;
import com.syntax.note.signupRequestPOJO.signupRequestBean;
import com.syntax.note.signupResponsePOJO.signupResponseBean;
import com.syntax.note.trashRequestPOJO.TrashRequestBean;
import com.syntax.note.trashResponsePOJO.TrashResponseBean;
import com.syntax.note.updateNoteRequestPOJO.updateNoteRequestBean;

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
    signup(@Body signupRequestBean body);

    //SigninforgotpassResponseBean
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<signinResponseBean>
    signin(@Body signinRequestBean body);

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

    //Trash
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<TrashResponseBean> trash
    (@Body TrashRequestBean body);

    //All Notes
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<allNoteResponseBean> allNotes
    (@Body TrashRequestBean body);

    //Delete Note
    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<allNoteResponseBean> deleteNote
    (@Body deleteNoteRequestBean body);


    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<CategoryResponseBean> emptyTrash
            (@Body CategoryRequestBean body);

    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<allNoteResponseBean> recoverNote
            (@Body deleteNoteRequestBean body);

    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<searchResultBean> search
            (@Body searchRequestBean body);

    @Headers({"Content-Type: application/json"})
    @POST("api/api.php")
    Call<searchResultBean> update
            (@Body updateNoteRequestBean body);

}