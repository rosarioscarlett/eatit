package com.example.izzy.preguntin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class crearPreguntaActivity extends AppCompatActivity {
EditText campoTitulo, campoPregunta;
private StorageReference referenciaHosting;
private DatabaseReference referenciaBaseDatos;
private DatabaseReference referenciaUsuario;
private DatabaseReference referenciaPreguntas;
private static final int GALLERY_REQUEST = 1;
private FirebaseAuth Auth;
private FirebaseUser UsuarioActual;
Uri imagen = null;
private ProgressDialog cargando;
private ImageButton campoImagen;
private Button enviarPregunta;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pregunta);

        campoTitulo = (EditText) findViewById(R.id.nuevoTitulo);
        campoPregunta = (EditText) findViewById(R.id.nuevaPregunta);

        Auth = FirebaseAuth.getInstance();

        UsuarioActual = Auth.getCurrentUser();

        referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(UsuarioActual.getUid());

        referenciaPreguntas = FirebaseDatabase.getInstance().getReference().child("Preguntas");

        campoImagen = (ImageButton) findViewById(R.id.nuevaImagen);

        campoImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeria2 = new Intent();

                galeria2.setType("image/*");

                galeria2.setAction(Intent.ACTION_GET_CONTENT);


                //startActivityForResult(galeria, GALLERY_REQUEST);
                startActivityForResult(Intent.createChooser(galeria2, "Seleccionar imágen..."), 2);
            }
        });

        enviarPregunta = (Button) findViewById(R.id.enviarButton);

        enviarPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearPregunta();
            }


        });










    }

    private void CrearPregunta() {
        cargando.setMessage("Enviando pregunta...");
        cargando.show();

        final String titulo = campoTitulo.getText().toString().trim();
        final String contenido = campoPregunta.getText().toString().trim();

        if (!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(contenido) && imagen != null){
            final DatabaseReference nuevaPregunta = referenciaPreguntas.push();


            referenciaUsuario.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nuevaPregunta.child("titulo").setValue(titulo);
                    nuevaPregunta.child("descripcion").setValue(contenido);
                    nuevaPregunta.child("nombre").setValue(UsuarioActual.getUid());
                    nuevaPregunta.child("autor").setValue(dataSnapshot.child("nombre").getValue());

                    final DatabaseReference imagenesRef = nuevoEvento.child("Imagenes");

                    StorageReference ubicacionparaUnaImagen = mStorage.child("ImagenesEvento").child(imagenok.getLastPathSegment());

                    ubicacionparaUnaImagen.putFile(imagenok).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadURLimagen = taskSnapshot.getDownloadUrl();
                            nuevoEvento.child("portada").setValue(downloadURLimagen.toString());
                        }
                    });


                    for (final Uri imagen: imagenes ){

                        StorageReference ubicacion = mStorage.child("ImagenesEvento").child(imagen.getLastPathSegment());

                        ubicacion.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();




                                imagenesRef.child("imagen"+imagen.getLastPathSegment()).setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(AgregarEventoActivity.this, MainActivity.class));
                                        }
                                    }

                                });

                                cargando.dismiss();




                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






        }
    }
}
