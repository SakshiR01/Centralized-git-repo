package com.gemini.Contripoint.service.interfaces;


public interface EncryptDecryptService {
    String encrypt(String voucher, String empEmail);

    String decrypt(String code);
}
