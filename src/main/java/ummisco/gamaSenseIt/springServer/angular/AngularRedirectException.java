package ummisco.gamaSenseIt.springServer.angular;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NeedRedirectToAngularExcepetion extends RuntimeException  {
}


    @Bean
    public AccessDeniedExceptionResolver getAccessDeniedExceptionResolver() {
        return new AccessDeniedExceptionResolver();
    }

public class AccessDeniedExceptionResolver implements HandlerExceptionResolver, Ordered
{
    private int order;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
    {
        if(exception instanceof NeedRedirectToAngularExcepetion)
        {
            String angular = (corsUrl.isEmpty()? "forward:/index.html" : "redirect:" + corsUrl + request.getRequestURI());
            logger.info("[302] Redirect to frontend at " + angular);
            return new ModelAndView(angular);
        }
        return null;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    @Override
    public int getOrder()
    {
        return order;
    }
}