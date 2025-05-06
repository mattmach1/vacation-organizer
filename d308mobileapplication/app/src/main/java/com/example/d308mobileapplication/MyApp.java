package com.example.d308mobileapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class MyApp extends Application {
    private String rawPassphrase;
    private byte[] dbPassphrase;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // 1) Set up MasterKey for EncryptedSharedPreferences
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // 2) Open or create secure SharedPreferences
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    this,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // 3) Get or generate the raw passphrase
            rawPassphrase = securePrefs.getString("db_passphrase", null);
            if (rawPassphrase == null) {
                rawPassphrase = UUID.randomUUID().toString();
                securePrefs.edit()
                        .putString("db_passphrase", rawPassphrase)
                        .apply();
            }
            // 4) Load SQLCipher libs and derive the byte[] key
            SQLiteDatabase.loadLibs(this);
            dbPassphrase = SQLiteDatabase.getBytes(rawPassphrase.toCharArray());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not set up encrypted prefs", e);
        }
    }

    public String getRawPassphrase() {
        return getRawPassphrase();
    }

    public byte[] getDbPassphrase() {
        return dbPassphrase;
    }
}

