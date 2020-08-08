package com.pet.webservice;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRetrofit {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "Authorization: Bearer"+" "+"QVS_9zulu_mfxNgk12UTJFUfU9o8K6awCRTmLJPd_-BkyeiabQ6NV_y55VsCE-4quCT9CL9OWqMJTgQMU3k6IdDvH_ArRR5N9uHoGHvDg4c54BvdaixmgZGX8zIh03sXVivrzpIU14-JxlK8lhCg5kkpZhLXAe6Qn74KGMSNoYOfay9X86MfoEzq8xl0ngrOxOE3-MBvVdSe5ytDCKqWgV1uilG8FT4jR111xnB6rmk"
    })
    @POST("saveRawJSONData")
    public Call<JsonObject> postRawJSON(@Body JsonObject locationPost);
}
