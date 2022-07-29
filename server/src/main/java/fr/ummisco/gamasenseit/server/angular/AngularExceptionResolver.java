package fr.ummisco.gamasenseit.server.angular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import fr.ummisco.gamasenseit.server.data.model.APIError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AngularExceptionResolver implements HandlerExceptionResolver, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AngularExceptionResolver.class);

    @Value("${gamaSenseIt.cors-url:}")
    private String corsUrl;
    private int order;

    @Override
    @Nullable
    public ModelAndView resolveException(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @Nullable Object handler,
            @NonNull Exception exception
    ) {
        if (exception instanceof AngularRedirectException) {
            String angular = "forward:/index.html"; // by default forward to angular
            if (corsUrl != null && !corsUrl.isEmpty())
                angular = "redirect:" + corsUrl + request.getRequestURI(); // if frontend, redirect to it
            logger.info("[REDIRECT] Redirect to angular : " + angular);
            return new ModelAndView(angular);
        } else if (exception instanceof ResponseStatusException respException) {
            var apiError = new APIError(respException.getStatus(), respException.getReason());
            logger.error("[" + respException.getStatus().value() + " " + respException.getStatus().getReasonPhrase() + "] " + respException.getReason());
            logger.debug(exception.getMessage(), exception);
            return apiError.modelAndView();
        }
        logger.warn("No handler for " + exception);
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}