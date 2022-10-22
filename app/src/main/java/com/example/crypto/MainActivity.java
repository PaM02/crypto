package com.example.crypto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputText,password;
    TextView outputText;
    String AES="AES";
    String outputString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        password = findViewById(R.id.password);
        Button encBtn = findViewById(R.id.encBtn);
        Button decBtn = findViewById(R.id.decBtn);
        encBtn.setOnClickListener(this);
        decBtn.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.encBtn:

                try {
                    outputString = encrypt(password.getText().toString());
                    outputText.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.decBtn:

                try {
                    outputString = decrypt(outputString,password.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"wrong password",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                outputText.setText(outputString);

                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec keySpec = generaKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE,keySpec);
        byte[] decodedValue = Base64.decode(outputString,Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String encrypt(String password) throws Exception {

        SecretKeySpec keySpec = generaKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte[] encVal = cipher.doFinal(password.getBytes());
        return Base64.encodeToString(encVal,Base64.DEFAULT);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private SecretKeySpec generaKey(String password) throws Exception {

        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        messageDigest.update(bytes,0,bytes.length);
        byte[] key = messageDigest.digest();
        return new SecretKeySpec(key,"AES");


    }
}
