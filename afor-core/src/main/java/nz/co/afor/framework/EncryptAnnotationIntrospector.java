package nz.co.afor.framework;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class EncryptAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated a) {
        Encrypted annotation = a.getAnnotation(Encrypted.class);
        if (annotation != null) {
            return new EncryptSerializer();
        }
        return super.findSerializer(a);
    }

    @Override
    public Object findDeserializer(Annotated a) {
        Encrypted annotation = a.getAnnotation(Encrypted.class);
        if (annotation != null) {
            return new EncryptDeserializer();
        }
        return super.findDeserializer(a);
    }
}
