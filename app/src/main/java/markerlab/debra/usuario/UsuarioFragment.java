package markerlab.debra.usuario;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import markerlab.debra.R;
import markerlab.debra.addeditUsuario.AddEditUsuarioActivity;
import markerlab.debra.data.UsuarioDbHelper;
import markerlab.debra.usuariodetail.UsuarioDetailActivity;

import static markerlab.debra.data.UsuarioContract.UsuarioEntry;


/**
 * Vista para la lista de usuarios
 */
public class UsuarioFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_USUARIO = 2;

    private UsuarioDbHelper mUsuarioDbHelper;

    private ListView mUsuariosList;
    private UsuarioCursorAdapter mUsuariosAdapter;
    private FloatingActionButton mAddButton;


    public UsuarioFragment() {
        // Required empty public constructor
    }

    public static UsuarioFragment newInstance() {
        return new UsuarioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_usuarios, container, false);

        // Referencias UI
        mUsuariosList = (ListView) root.findViewById(R.id.usuarios_list);
        mUsuariosAdapter = new UsuarioCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mUsuariosList.setAdapter(mUsuariosAdapter);

        // Eventos
        mUsuariosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mUsuariosAdapter.getItem(i);
                String currentLawyerId = currentItem.getString(
                        currentItem.getColumnIndex(UsuarioEntry.ID));

                showDetailScreen(currentLawyerId);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });


        getActivity().deleteDatabase(UsuarioDbHelper.DATABASE_NAME);

        // Instancia de helper
        mUsuarioDbHelper = new UsuarioDbHelper(getActivity());

        // Carga de datos
        loadLawyers();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case AddEditUsuarioActivity.REQUEST_ADD_USUARIO:
                    showSuccessfullSavedMessage();
                    loadLawyers();
                    break;
                case REQUEST_UPDATE_DELETE_USUARIO:
                    loadLawyers();
                    break;
            }
        }
    }

    private void loadLawyers() {
        new LawyersLoadTask().execute();
    }

    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditUsuarioActivity.class);
        startActivityForResult(intent, AddEditUsuarioActivity.REQUEST_ADD_USUARIO);
    }

    private void showDetailScreen(String lawyerId) {
        Intent intent = new Intent(getActivity(), UsuarioDetailActivity.class);
        intent.putExtra(UsuarioActivity.EXTRA_USUARIO_ID, lawyerId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_USUARIO);
    }

    private class LawyersLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mUsuarioDbHelper.getAllUsuario();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mUsuariosAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

}
