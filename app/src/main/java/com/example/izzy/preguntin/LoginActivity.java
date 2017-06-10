package com.example.izzy.preguntin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "g55b b";
    Button mLogin;
    Button mRegistrar;
    EditText mEmail;
    EditText mPassword;
    FirebaseAuth Autentificacion;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    DatabaseReference databaseReferenceUsuarios;


    ProgressDialog cargando;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin=(Button)findViewById(R.id.btnLogin) ;
        mRegistrar=(Button)findViewById(R.id.btnRegistrar) ;
        mEmail=(EditText) findViewById(R.id.txtEmail) ;
        mPassword=(EditText) findViewById(R.id.txtPassword) ;

        Autentificacion = FirebaseAuth.getInstance();
        cargando = new ProgressDialog(this);

        databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrarseIntent = new Intent(LoginActivity.this,RegistrarActivity.class);
                registrarseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registrarseIntent);
            }
        });




    }

    private void iniciarSesion() {
        String Email = mEmail.getText().toString().trim();
        String Password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){

            cargando.setMessage("Iniciando Sesión...");
            cargando.show();
            Autentificacion.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        cargando.dismiss();

                        Existeusuario();

                    }else{
                        cargando.dismiss();
                        Toast.makeText(LoginActivity.this, "Error al Iniciar Sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void Existeusuario() {
        if (Autentificacion.getCurrentUser() != null){

                final String user_id = Autentificacion.getCurrentUser().getUid();
                databaseReferenceUsuarios.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            Intent PrincipalIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            PrincipalIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(PrincipalIntent);
                        } else {
                            Intent ConfigurarIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            ConfigurarIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(ConfigurarIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }

    }

}
