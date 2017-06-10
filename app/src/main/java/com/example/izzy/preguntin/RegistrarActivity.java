package com.example.izzy.preguntin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {

    Button mRegistrar;
    EditText mEmail;
    EditText mNombre;
    EditText mPassword;

    ProgressDialog cargando;
    DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        mRegistrar=(Button)findViewById(R.id.registrarBoton);
        mEmail= (EditText)findViewById(R.id.emailField);
        mNombre= (EditText)findViewById(R.id.nombreField);
        mPassword= (EditText)findViewById(R.id.passwordField);

        cargando = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios"); // ir a la raiz de mi bd
        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });



    }

    public void registrar(){
        final String nombre = mNombre.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            cargando.setMessage("Registrando usuario en Preguntin...");
            cargando.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference usuario_actual =  mDatabaseReference.child(user_id);

                        usuario_actual.child("nombre").setValue(nombre);
                        usuario_actual.child("email").setValue(email);
                        usuario_actual.child("password").setValue(password);
                        usuario_actual.child("imagen").setValue("pordefecto");

                        cargando.dismiss();

                        Intent principalintent = new Intent(RegistrarActivity.this, HomeActivity.class);
                        principalintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(principalintent);

                    }
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegistrarActivity.this, task.getException().toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
