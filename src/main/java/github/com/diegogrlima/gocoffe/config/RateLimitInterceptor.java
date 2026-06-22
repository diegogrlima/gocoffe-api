package github.com.diegogrlima.gocoffe.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 10;
    private static final int WINDOW_SECONDS = 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String key = getClientIP(request) + ":" + request.getRequestURI();

        RateLimitBucket bucket = buckets.compute(key, (k, existing) -> {
            if (existing == null || existing.isExpired()) {
                return new RateLimitBucket();
            }
            return existing;
        });

        if (bucket.incrementAndGet() > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            try {
                response.getWriter().write("""
                        {
                            "status": 429,
                            "error": "Too Many Requests",
                            "message": "Rate limit exceeded. Try again later."
                        }
                        """);
            } catch (Exception e) {
                // ignore
            }
            return false;
        }

        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RateLimitBucket {
        private final AtomicInteger count = new AtomicInteger(0);
        private final long createdAt = System.currentTimeMillis();

        public int incrementAndGet() {
            return count.incrementAndGet();
        }

        public boolean isExpired() {
            return (System.currentTimeMillis() - createdAt) > (WINDOW_SECONDS * 1000L);
        }
    }
}
