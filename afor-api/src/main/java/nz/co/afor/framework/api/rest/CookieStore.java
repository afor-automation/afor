package nz.co.afor.framework.api.rest;

import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("thread")
@Component
public class CookieStore extends BasicCookieStore {
}
