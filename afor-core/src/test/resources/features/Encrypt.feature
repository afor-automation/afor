Feature: Encryption and decryption

  As an automated tester
  I want to be able to encrypt and decrypt data
  So that I do not store sensitive information in plain text

  Scenario: Encrypt and decrypt a string
    Given I have an unencrypted string "test unencrypted string"
    When I encrypt the string
    Then the encrypted string should be encrypted
    When I decrypt the encrypted string
    Then the decrypted string should match the unencrypted string

  Scenario: Generate keys
    When I generate a 128 bit key
    Then I should be able to base64 encode the key
    When I generate a key with the password "test" and salt "testing"

  Scenario: Generate key and iv
    When I generate a key and iv

  Scenario: Decrypt values in property files
    Then I should have a decrypted value from the properties