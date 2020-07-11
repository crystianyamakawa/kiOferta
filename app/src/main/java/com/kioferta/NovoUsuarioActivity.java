package com.kioferta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NovoUsuarioActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void cadastrar_usuario(View view){

        EditText email = (EditText) findViewById(R.id.txEmail);// edittext
        EditText password = (EditText) findViewById(R.id.txsenha);// edittext

        if (password.getText().toString().length()>5){

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(NovoUsuarioActivity.this, "Conta Criada com sucesso!!!.",
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(NovoUsuarioActivity.this, "Erro ao criar conta.",
                                    Toast.LENGTH_SHORT).show();

                            if (!task.isSuccessful()) {
                                Log.e("TAG", "onComplete: Failed=" + task.getException().getMessage());
                            }

                        }

                        // ...
                    }
                });
        } else {
            Toast.makeText(NovoUsuarioActivity.this, "Informar uma senha maior de 6 digitos.",
                    Toast.LENGTH_SHORT).show();

        }


    }
}
