package info.androidhive.navigationdrawer.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import info.androidhive.navigationdrawer.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int DEFAULT_SPEED = 1;
    private static final double DEFAULT_TOLERANCE = 5;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView spaceship;
    private boolean leftPressed;
    private boolean rightPressed;


    private OnFragmentInteractionListener mListener;
    private SensorManager manager;
    public Sensor rotationVector;
    private int screenWidth;
    private int screenHeight;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        spaceship = (ImageView) view.findViewById(R.id.spaceship);
        manager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        rotationVector = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        manager.unregisterListener(this);
        super.onPause();
    }

    private boolean wouldSpaceshipBeInWindow(int speed) {
        float value = spaceship.getX() + speed;
        return !((value < 0) || value + spaceship.getWidth() > screenWidth);
    }

    //set center of spaceship to provided positions
    public void moveSpaceship(int positionX, int positionY) {
        int posX = positionX - spaceship.getWidth() / 2;
        spaceship.setX(posX);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float aX, aY, aZ;
        leftPressed = false;
        rightPressed = false;
        aX = event.values[0];
        aY = event.values[1];
        aZ = event.values[2];
        double angle = Math.toDegrees(aY);
        int posX = 0;
        double anglePercentage = angle / 45;
        posX = (int) ((screenWidth / 2) + (screenWidth / 2) * anglePercentage);
        Log.e(TAG, "onSensorChanged: angle :" + angle + ", anglePercentage :" + anglePercentage);
        moveSpaceship(posX, (int) spaceship.getY());

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
