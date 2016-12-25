package markerlab.debra.addeditUsuario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import markerlab.debra.R;
import markerlab.debra.usuario.UsuarioActivity;

public class AddEditUsuarioActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_USUARIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String usuarioId = getIntent().getStringExtra(UsuarioActivity.EXTRA_USUARIO_ID);

        setTitle(usuarioId == null ? "Añadir usuario" : "Editar usuario");

        AddEditUsuarioFragment addEditUsuarioFragment = (AddEditUsuarioFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_edit_usuario_container);
        if (addEditUsuarioFragment == null) {
            addEditUsuarioFragment = AddEditUsuarioFragment.newInstance(usuarioId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_usuario_container, addEditUsuarioFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
