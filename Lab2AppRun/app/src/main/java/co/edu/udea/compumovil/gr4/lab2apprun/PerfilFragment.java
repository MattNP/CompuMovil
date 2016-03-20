package co.edu.udea.compumovil.gr4.lab2apprun;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView txt_user, txt_correo;



    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.PREF_USUARIO, Context.MODE_PRIVATE);

        txt_user = (TextView) getActivity().findViewById(R.id.textViewUsuario);
        txt_correo = (TextView) getActivity().findViewById(R.id.textViewCorreo);



        String usuario = sharedPreferences.getString(MainActivity.USER, "Usuario x");
        String correo = sharedPreferences.getString(MainActivity.CORREO, "Correo x");

        Log.d("onCreateView", usuario);
        Log.d("onCreateView", correo);

        txt_user.setText(usuario);
        txt_correo.setText(correo);
        */

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

}
