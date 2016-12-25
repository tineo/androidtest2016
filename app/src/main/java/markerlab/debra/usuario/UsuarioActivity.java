package markerlab.debra.usuario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import markerlab.debra.R;

public class UsuarioActivity extends AppCompatActivity {

    public static final String EXTRA_USUARIO_ID = "extra_usuario_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UsuarioFragment fragment = (UsuarioFragment)
                getSupportFragmentManager().findFragmentById(R.id.usuarios_container);

        if (fragment == null) {
            fragment = UsuarioFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.usuarios_container, fragment)
                    .commit();
        }
    }
}
