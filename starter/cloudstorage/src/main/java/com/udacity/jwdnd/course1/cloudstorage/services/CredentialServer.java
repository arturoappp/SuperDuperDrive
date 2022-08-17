package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.util.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.util.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialServer {
    @Autowired
    CredentialMapper credentialMapper;

    @Autowired
    HashService hashService;

    @Autowired
    EncryptionService encryptionService;

    public int create(Credential credential) {

        if (credentialMapper.getCredential(credential.getUrl()) != null) {
            throw new RuntimeException("Credential already exist for this URL!");
        }

        byte[] salt = new byte[16];
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), encodedSalt);

        credential.setKey(encodedSalt);
        credential.setPassword(encryptPassword);

        return credentialMapper.save(credential);
    }

    public List<Credential> findAllByUserId(int userId) {
        return credentialMapper.findAll(userId)
                .stream()
                .peek(credential -> {
                            String key = credential.getKey();
                            credential.setKey(credential.getPassword());
                            credential.setPassword(encryptionService.decryptValue(credential.getPassword(), key));
                        }
                )
                .collect(Collectors.toList());
    }

    public Credential findById(int id) {
        return credentialMapper.findById(id);
    }

    public void delete(int id) {
        credentialMapper.delete(id);
    }

    public void update(Credential credential) {
        String key = findById(credential.getCredentialId()).getKey();

        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encryptPassword);
        credentialMapper.update(credential);
    }
}
