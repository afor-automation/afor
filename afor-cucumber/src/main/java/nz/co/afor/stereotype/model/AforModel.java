package nz.co.afor.stereotype.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope("thread")
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface AforModel {
}
