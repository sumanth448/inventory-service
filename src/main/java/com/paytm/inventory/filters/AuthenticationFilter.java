package com.paytm.inventory.filters;


import com.paytm.inventory.constants.InventoryConstants;
import com.paytm.inventory.exception.AuthorizationException;
import com.paytm.inventory.models.User;
import com.paytm.inventory.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter{

    @Autowired
    private UserService userService;

    public AuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if(!httpRequest.getRequestURI().contains("swagger") && !httpRequest.getRequestURI().contains("api-docs") ) {
            String userName = httpRequest.getHeader(InventoryConstants.USERNAME);
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            try{
                User user = userService.getUserDetails(userName);
                httpRequest.setAttribute(InventoryConstants.USER, user);
            }catch(AuthorizationException ex){
                response.setStatus(HttpStatus.FORBIDDEN.value());
                JSONObject obj = new JSONObject();
                try {
                    obj.put("error_code", ex.getErrorCode());
                    obj.put("error_message", ex.getErrorMessage());
                    response.getOutputStream().print(obj.toString());
                    return;
                } catch (JSONException e) {
                    throw e;
                }
            }

        }
        filterChain.doFilter(httpRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
