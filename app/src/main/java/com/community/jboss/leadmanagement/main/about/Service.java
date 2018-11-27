package com.community.jboss.leadmanagement.main.about;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("repos/JBossOutreach/lead-management-android/contributors")
    Call<List<Contributor>> getContributors();

}
