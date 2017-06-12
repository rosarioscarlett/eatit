package com.example.izzy.preguntin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.izzy.preguntin.clases.Pregunta;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mListaPreguntas;

    private DatabaseReference mDBPreguntas;

    private DatabaseReference mDatabaseImagenes;

    DatabaseReference mDBUsuarios;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent LoginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }

            }
        };

        mListaPreguntas = (RecyclerView) findViewById(R.id.listaPreguntas);
        mListaPreguntas.setHasFixedSize(true);
        mListaPreguntas.setLayoutManager(new LinearLayoutManager(this));

        mDBPreguntas = FirebaseDatabase.getInstance().getReference().child("Preguntas");
        //mDatabaseImagenes = FirebaseDatabase.getInstance().getReference().child("Preguntas");

        mDBUsuarios = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        mDBUsuarios.keepSynced(true);

        //Existeusuario();
    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);

        FirebaseRecyclerAdapter<Pregunta, PreguntaViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Pregunta, PreguntaViewHolder>(
                Pregunta.class,
                R.layout.pregunta_layout,
                PreguntaViewHolder.class,
                mDBPreguntas
        ) {
            @Override
            protected void populateViewHolder(PreguntaViewHolder viewHolder, Pregunta model, int position) {

                final String pregunta_key = getRef(position).getKey();

                viewHolder.setTitulo(model.getTitulo());

                viewHolder.setDescripcion(model.getDescripcion());

                viewHolder.setImagen(getApplicationContext(), model.getImagen());

                viewHolder.setCredito(model.getCredito());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singlePreguntaIntent = new Intent(HomeActivity.this, RegistrarActivity.class);
                        singlePreguntaIntent.putExtra("Pregunta_id", pregunta_key);

                        startActivity(singlePreguntaIntent);

                    }
                });

            }

        };

        mListaPreguntas.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView item_titulo;
        public PreguntaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            item_titulo = (TextView) mView.findViewById(R.id.item_titulo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        public void setTitulo(String titulo){
            //TextView item_titulo = (TextView) mView.findViewById(R.id.titulo_item);
            item_titulo.setText(titulo);
        }

        public void setDescripcion(String descripcion){
            TextView item_descripcion = (TextView) mView.findViewById(R.id.item_descripcion);
            item_descripcion.setText(descripcion);
        }

        public void setImagen(final Context ctx, final String imagen){
            final ImageView item_imagen = (ImageView) mView.findViewById(R.id.item_imagen);
            Picasso.with(ctx).load(imagen).into(item_imagen);

        }
        public void setCredito(int credito){
            TextView item_creditos = (TextView) mView.findViewById(R.id.item_creditos);
            item_creditos.setText("Creditos: " +String.valueOf(credito));
        }


    }

}
