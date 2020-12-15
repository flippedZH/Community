package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTo;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Component //加了这个，就可以把这部分注入目标代码的上下文中，（先是实例化后，放在容器中的）
public class GithubProvider {
    //参数超过两个：封装成对象去做
    public String getAccessToken(AccessTokenDTo accessTokenDTo){

        //post请求 ：为了得到accesstoken
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");
         OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTo));
        Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string =response.body().string();
                String token =string.split("&")[0].split("=")[1];
               System.out.println(token); // 这个就是令牌（会变化的）
                return  token;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    //GetUrl:
    public GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder()
                //github新版请求方式
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
//               .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String string =response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);//把string自动转账为java类对象,不用解析string
            return githubUser;
        } catch (IOException e){

        }
        return null;
    }
}

