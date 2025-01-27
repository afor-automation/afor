package nz.co.afor.framework;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
class EncryptConfig implements ApplicationContextAware, InitializingBean {

    @Value("${nz.co.afor.encrypt.key:${ENCRYPT_KEY:}}")
    private String base64EncodedKey;
    private SecretKey key;

    @Value("${nz.co.afor.encrypt.algorithm:AES/CBC/PKCS5Padding}")
    private String algorithm;

    @Value("${nz.co.afor.encrypt.key.algorithm:AES}")
    private String keyAlgorithm;

    @Value("${nz.co.afor.encrypt.iv:${ENCRYPT_IV:}}")
    private String base64EncodedIv;
    private IvParameterSpec iv;
    private static ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws NoSuchAlgorithmException {
        if (null != base64EncodedKey && !base64EncodedKey.isEmpty()) {
            byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
            this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, keyAlgorithm);
        } else {
            this.key = Encrypt.generateKey(128);
        }
        if (null != base64EncodedIv && !base64EncodedIv.isEmpty()) {
            byte[] decodedIv = Base64.getDecoder().decode(base64EncodedIv);
            this.iv = new IvParameterSpec(decodedIv);
        } else {
            this.iv = Encrypt.generateIv();
        }
    }

    protected SecretKey getKey() {
        return key;
    }

    protected String getAlgorithm() {
        return algorithm;
    }

    protected String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    protected IvParameterSpec getIv() {
        return iv;
    }

    protected static EncryptConfig getEncryptConfig() {
        return applicationContext.getBean(EncryptConfig.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EncryptConfig.applicationContext = applicationContext;
    }
}
