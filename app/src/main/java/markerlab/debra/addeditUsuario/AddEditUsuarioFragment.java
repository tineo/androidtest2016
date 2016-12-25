package markerlab.debra.addeditUsuario;


import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import markerlab.debra.R;
import markerlab.debra.data.Usuario;
import markerlab.debra.data.UsuarioDbHelper;

/**
 * Vista para creación/edición de un usuario
 */
public class AddEditUsuarioFragment extends Fragment {
    private static final String ARG_USUARIO_ID = "arg_usuario_id";

    private String mUsuarioId;

    private UsuarioDbHelper mUsuarioDbHelper;

    private FloatingActionButton mSaveButton;
    private TextInputEditText mNameField;
    private TextInputEditText mPhoneNumberField;
    private TextInputEditText mSpecialtyField;
    private TextInputEditText mBioField;
    private TextInputLayout mNameLabel;
    private TextInputLayout mPhoneNumberLabel;
    private TextInputLayout mSpecialtyLabel;
    private TextInputLayout mBioLabel;


    public AddEditUsuarioFragment() {
        // Required empty public constructor
    }

    public static AddEditUsuarioFragment newInstance(String usuarioId) {
        AddEditUsuarioFragment fragment = new AddEditUsuarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USUARIO_ID, usuarioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsuarioId = getArguments().getString(ARG_USUARIO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_usuario, container, false);

        // Referencias UI
        mSaveButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mNameField = (TextInputEditText) root.findViewById(R.id.et_name);
        mPhoneNumberField = (TextInputEditText) root.findViewById(R.id.et_phone_number);
        mSpecialtyField = (TextInputEditText) root.findViewById(R.id.et_specialty);
        mBioField = (TextInputEditText) root.findViewById(R.id.et_bio);
        mNameLabel = (TextInputLayout) root.findViewById(R.id.til_name);
        mPhoneNumberLabel = (TextInputLayout) root.findViewById(R.id.til_phone_number);
        mSpecialtyLabel = (TextInputLayout) root.findViewById(R.id.til_specialty);
        mBioLabel = (TextInputLayout) root.findViewById(R.id.til_bio);

        // Eventos
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditUsuario();
            }
        });

        mUsuarioDbHelper = new UsuarioDbHelper(getActivity());

        // Carga de datos
        if (mUsuarioId != null) {
            loadUsuario();
        }

        return root;
    }

    private void loadUsuario() {
        new GetUsuarioByIdTask().execute();
    }

    private void addEditUsuario() {
        boolean error = false;

        String name = mNameField.getText().toString();
        String phoneNumber = mPhoneNumberField.getText().toString();
        String specialty = mSpecialtyField.getText().toString();
        String bio = mBioField.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mNameLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(specialty)) {
            mSpecialtyLabel.setError(getString(R.string.field_error));
            error = true;
        }


        if (TextUtils.isEmpty(bio)) {
            mBioLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (error) {
            return;
        }

        Usuario usuario = new Usuario(name, specialty, phoneNumber, bio, "");

        new AddEditUsuarioTask().execute(usuario);

    }
    private void showUsuarioScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            getActivity().setResult(Activity.RESULT_OK);
        }

        getActivity().finish();
    }

    private void showAddEditError() {
        Toast.makeText(getActivity(),
                "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
    }

    private void showLawyer(Usuario usuario) {
        mNameField.setText(usuario.getName());
        mPhoneNumberField.setText(usuario.getPhoneNumber());
        mSpecialtyField.setText(usuario.getDireccion());
        mBioField.setText(usuario.getEmail());
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al editar usuario", Toast.LENGTH_SHORT).show();
    }

    private class GetUsuarioByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mUsuarioDbHelper.getUsuarioById(mUsuarioId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showLawyer(new Usuario(cursor));
            } else {
                showLoadError();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        }

    }

    private class AddEditUsuarioTask extends AsyncTask<Usuario, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Usuario... usuarios) {
            if (mUsuarioId != null) {
                return mUsuarioDbHelper.updateUsuario(usuarios[0], mUsuarioId) > 0;

            } else {
                return mUsuarioDbHelper.saveUsuario(usuarios[0]) > 0;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            showUsuarioScreen(result);
        }

    }

}
