package com.ust.Book_Gateway.filter;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    @Autowired
    RouteValidator validator;

    public AuthenticationFilter(){
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
                if(validator.isSecured.test(exchange.getRequest())){
                    if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                        throw new RuntimeException("Missing Authorization Header");
                    }
                    String authHeadToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if(authHeadToken!=null && authHeadToken.startsWith("Bearer")){
                        authHeadToken = authHeadToken.substring(7);
                    }
                    try{
                        RestClient restClient = RestClient.create();
                        restClient.get()
                                .uri("http://authentication-sr:9292/api/auth/validate/token?token="+authHeadToken)
                                .retrieve()
                                .body(Boolean.class);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException("Invalid Access!! : " + e.getMessage());
                    }
                }
            return chain.filter(exchange);
        });
    }



}