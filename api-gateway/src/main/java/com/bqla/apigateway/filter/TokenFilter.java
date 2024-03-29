package com.bqla.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Slf4j
@Component
public class TokenFilter extends ZuulFilter {

    /**
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     *
     * pre：可以在请求被路由之前调用
     * route：在路由请求时候被调用
     * post：在route和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     *      Zuul的主要请求生命周期包括“pre”，“route”和“post”等阶段。对于每个请求，都会运行具有这些类型的所有过滤器。
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * filterOrder：通过int值来定义过滤器的执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER-1;
    }

    /**
     * shouldFilter：返回一个boolean类型来判断该过滤器是否要执行，
     * 所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，
     * 不对其进行路由，然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码
     * @return
     */
    @Override
    public Object run() {

        log.info("TokenFilter 开始进入");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        log.info("TokenFilter 结束");
        return null;
    }
}
