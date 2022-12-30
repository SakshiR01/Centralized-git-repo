package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.service.interfaces.EncryptDecryptService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptDecryptServiceImp implements EncryptDecryptService {

    @Override
    public String encrypt(String voucher, String empEmail) {
        String message = voucher;
        String password = "C0nTr1p0!nT";

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(password);
        String basicEncryptedMessage = basicTextEncryptor.encrypt(message);
        return basicEncryptedMessage;
    }

    @Override
    public String decrypt(String code) {
        String message = code;
        String password = "C0nTr1p0!nT";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(password);
        String decryptedPassword = basicTextEncryptor.decrypt(message);
        return decryptedPassword;
    }

}
