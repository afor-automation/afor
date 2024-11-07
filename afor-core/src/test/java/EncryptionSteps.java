import io.cucumber.java8.En;
import nz.co.afor.framework.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class EncryptionSteps implements En {

    private static final Logger log = LoggerFactory.getLogger(EncryptionSteps.class);

    private String unencryptedString;
    private String encryptedString;
    private String decryptedString;
    private SecretKey key;

    @Value("${nz.co.afor.encryptedvalue}")
    String decryptedValue;

    public EncryptionSteps() {
        Given("I have an unencrypted string {string}", (String unencryptedString) -> this.unencryptedString = unencryptedString);
        When("I encrypt the string", () -> this.encryptedString = Encrypt.encrypt(unencryptedString));
        Then("the encrypted string should be encrypted", () -> assertThat("The string should be encrypted", encryptedString, not(equalTo(unencryptedString))));
        When("I decrypt the encrypted string", () -> this.decryptedString = Encrypt.decrypt(encryptedString));
        Then("the decrypted string should match the unencrypted string", () -> assertThat("The string should be decrypted", decryptedString, equalTo(unencryptedString)));
        When("I generate a {int} bit key", (Integer bits) -> this.key = Encrypt.generateKey(bits));
        When("I generate a key with the password {string} and salt {string}", (String password, String salt) -> this.key = Encrypt.generateKey(password, salt));
        Then("I should be able to base64 encode the key", () -> {
            String encodedString = Base64.getEncoder().encodeToString(this.key.getEncoded());
            log.info("Generated key '{}'", encodedString);
        });
        When("I generate a key and iv", Encrypt::generateKeyAndIv);
        Then("I should have a decrypted value from the properties", () -> assertThat(decryptedValue, equalTo("password1")));
    }
}
