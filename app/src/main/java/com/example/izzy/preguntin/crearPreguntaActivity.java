package com.example.izzy.preguntin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class crearPreguntaActivity extends AppCompatActivity {
EditText campoTitulo, campoPregunta;
private StorageReference referenciaHosting;
private DatabaseReference referenciaBaseDatos;
private DatabaseReference referenciaUsuario;
private static final int GALLERY_REQUEST = 1;
private FirebaseAuth Auth;
private FirebaseUser UsuarioActual;
Uri imagen = null;
private ProgressDialog cargando;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pregunta);

        campoTitulo = (EditText) findViewById(R.id.nuevoTitulo);
        campoPregunta = (EditText) findViewById(R.id.nuevaPregunta);

        referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(UsuarioActual.getUid());










    }
}
