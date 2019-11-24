package com.community.provider;

import com.alibaba.fastjson.JSON;
import com.community.dto.AccessTokenDTO;
import com.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component //有了这个注解就可以很轻松的拿到直接出来用 不用去实例化一个对象（new）自动实例化在AuthController里面用到了
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token") //第二步调用access_token接口
                .post(body)  //post接口
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =  response.body().string();
            System.out.println("拆分前的string：" +string);//返回的string是 access_token=bc6ac877e2a99e9a9cba63d308a306025bc35800&scope=user&token_type=bearer
            //所以要把string进行拆分
            String[] split =string.split("&");//用&与号区分
            String tokenstr = split[0];
            String token = tokenstr.split("=")[1];//拿到token
            return token;                                     //第三步返回accesstoken
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //第四步通过access_token去获取用户的信息
  public GithubUser getUser(String accessToken){
      OkHttpClient client = new OkHttpClient();
          Request request = new Request.Builder()
                  .url("https://api.github.com/user?access_token="+accessToken)
                  .build();

      try {
          Response response = client.newCall(request).execute();
          String string =  response.body().string();
          GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//用到了fastjson这个包，这一步的作用是把string的json对象自动转换解析成java的类对象
          return githubUser;       //第五步返回user信息
      } catch (IOException e) {
          e.printStackTrace();
      }
     return null;
  }

}
