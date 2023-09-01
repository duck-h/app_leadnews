package com.heima.app.gateway.filter;


import com.heima.app.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.獲取request和response對象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.判斷是否為登入狀態
        if (request.getURI().getPath().contains("/login")) {
            //放行
            return chain.filter(exchange);
        }
        //3.獲取token
        String token = request.getHeaders().getFirst("token");

        //4.判斷token是否存在
        if (StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.判斷token是否有效
        try {

            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            //token是否過期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2) { //過期
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //6.放行

        return chain.filter(exchange);
    }


    //優先級設置 值越小 優先級越高
    @Override
    public int getOrder() {
        return 0;
    }
}
