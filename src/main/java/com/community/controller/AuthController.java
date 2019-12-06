package com.community.controller;

import com.community.dto.AccessTokenDTO;
import com.community.dto.GithubUser;
import com.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @Autowired//写一个注解再这样定义一下就会帮刚才实例化好的东西放在里面直接用
    private GithubProvider githubProvider;

    @Value("${github.client.id}")//意思是会去配置文件里去读这个github.client.id key值的value然后赋值给clientId这个变量
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;

    @GetMapping("/callback")
    public String Callback(@RequestParam(name ="code")String code,
                           @RequestParam(name ="state")String state,
                           HttpServletRequest request //相当于jsp里的request 要使用就需要先声明一下request，spring就自动给你
                           ){
        //githubProvider.getAccessToken(new AccessTokenDTO());按住ctrl alt +v 就可以变成下面两句 快速创建一个AccessTokenDTO对象
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirecturi);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if(user != null){
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else{
            return "redirect:/";
        }
        //System.out.println(user.getName()); //获取到user的name、BIO等信息
        //System.out.println(user.getBio());
    }
}
