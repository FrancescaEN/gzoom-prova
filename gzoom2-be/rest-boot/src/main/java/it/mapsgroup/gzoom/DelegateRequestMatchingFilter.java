package it.mapsgroup.gzoom;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Andrea Fossi.
 */
public class DelegateRequestMatchingFilter implements Filter {
    private Filter delegate;
    private RequestMatcher ignoredRequests;

    public DelegateRequestMatchingFilter(RequestMatcher ignoredRequests, Filter delegate) {
        this.ignoredRequests = ignoredRequests;
        this.delegate = delegate;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        delegate.init(filterConfig);
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (ignoredRequests.matches(request)) {
            chain.doFilter(req, resp);
        } else {
            delegate.doFilter(req, resp, chain);
        }
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }
}
