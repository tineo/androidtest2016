package markerlab.debra.usuariodetail;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import markerlab.debra.R;
import markerlab.debra.addeditUsuario.AddEditUsuarioActivity;
import markerlab.debra.data.Usuario;
import markerlab.debra.data.UsuarioDbHelper;
import markerlab.debra.usuario.UsuarioActivity;
import markerlab.debra.usuario.UsuarioFragment;


public class UsuarioDetailFragment extends Fragment {
    private static final String ARG_USUARIO_ID = "usuarioId";

    private String mUsuarioId;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mPhoneNumber;
    private TextView mSpecialty;
    private TextView mBio;

    private UsuarioDbHelper mUsuarioDbHelper;


    public UsuarioDetailFragment() {
        // Required empty public constructor
    }

    public static UsuarioDetailFragment newInstance(String UsuarioId) {
        UsuarioDetailFragment fragment = new UsuarioDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USUARIO_ID, UsuarioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUsuarioId = getArguments().getString(ARG_USUARIO_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_usuario_detail, container, false);
        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) getActivity().findViewById(R.id.iv_avatar);
        mPhoneNumber = (TextView) root.findViewById(R.id.tv_phone_number);
        mSpecialty = (TextView) root.findViewById(R.id.tv_specialty);
        mBio = (TextView) root.findViewById(R.id.tv_bio);

        mUsuarioDbHelper = new UsuarioDbHelper(getActivity());

        loadLawyer();

        return root;
    }

    private void loadLawyer() {
        new GetLawyerByIdTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                new DeleteLawyerTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UsuarioFragment.REQUEST_UPDATE_DELETE_USUARIO) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }

    private void showLawyer(Usuario usuario) {
        mCollapsingView.setTitle(usuario.getName());
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + usuario.getAvatarUri()))
                .centerCrop()
                .into(mAvatar);
        mPhoneNumber.setText(usuario.getPhoneNumber());
        mSpecialty.setText(usuario.getDireccion());
        mBio.setText(usuario.getEmail());
    }

    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), AddEditUsuarioActivity.class);
        intent.putExtra(UsuarioActivity.EXTRA_USUARIO_ID, mUsuarioId);
        startActivityForResult(intent, UsuarioFragment.REQUEST_UPDATE_DELETE_USUARIO);
    }

    private void showLawyersScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
        }
        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
    }

    private class GetLawyerByIdTask extends AsyncTask<Void, Void, Cursor> {

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
            }
        }

    }

    private class DeleteLawyerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mUsuarioDbHelper.deleteUsuario(mUsuarioId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showLawyersScreen(integer > 0);
        }

    }

}
