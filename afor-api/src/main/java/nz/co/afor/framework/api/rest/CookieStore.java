package nz.co.afor.framework.api.rest;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("thread")
@Component
public class CookieStore extends BasicCookieStore {
}
