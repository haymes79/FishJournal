package au.com.mitchhaley.fishjournal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;

/**
 * Created by mitch on 18/10/13.
 */
public class FishConditionsFragment extends Fragment
{
    private final String[] tideValues = {"NA", "High", "Low"};
    
    private final String[] moonValues = {"New Moon", "First Quarter", "Full Moon", "Last Quarter"};

    private final String[] conditionValues = {"Sunny", "Cloudy", "Storms", "Light Rain", "Snow"};

    private final int[] moonImages = {R.drawable.new_moon, R.drawable.first_quarter_moon, R.drawable.full_moon, R.drawable.last_quarter_moon};

    private final int[] conditionImages = {R.drawable.sunny, R.drawable.cloudy, R.drawable.tstorm, R.drawable.light_rain, R.drawable.snow};

    private Spinner mConditionSpinner;

    private Spinner mMoonSpinner;

    private Spinner mTideSpinner;
    
    private TextView mTemperatureSeekBarValue;  

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishConditionsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fishentry_conditions, container, false);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBarTemperature);
        mTemperatureSeekBarValue = (TextView) view.findViewById(R.id.temperatureValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            	mTemperatureSeekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mConditionSpinner = (Spinner) view.findViewById(R.id.spinnerConditions);
        MyAdapter conditionsAdapter = new MyAdapter(getActivity(), R.layout.image_spinner_row, conditionValues);
        conditionsAdapter.setImages(conditionImages);
        mConditionSpinner.setAdapter(conditionsAdapter);

        mTideSpinner = (Spinner) view.findViewById(R.id.tideSpinner);
        mTideSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tideValues));

        mMoonSpinner = (Spinner) view.findViewById(R.id.spinnerMoon);
        MyAdapter moonAdapter = new MyAdapter(getActivity(), R.layout.image_spinner_row, moonValues);
        moonAdapter.setImages(moonImages);
        mMoonSpinner.setAdapter(moonAdapter);


        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {

        String[] values;
        int[] images;

        public MyAdapter(Context context, int textViewResourceId, String[] values) {
            super(context, textViewResourceId, values);
            this.values = values;
        }

        public void setImages(int[] images) {
            this.images = images;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater(new Bundle());
            View row=inflater.inflate(R.layout.image_spinner_row, parent, false);
            TextView label=(TextView) row.findViewById(R.id.rowTextValue);
            label.setText(values[position]);

            ImageView icon=(ImageView) row.findViewById(R.id.rowImage);
            icon.setImageResource(images[position]);

            return row;
        }
    }
    
    public String getCondition() {
    	return mConditionSpinner.getSelectedItem().toString();
    }
    
    public String getTemperature() {
    	return mTemperatureSeekBarValue.getText().toString();
    }

    public String getMoon() {
        return mMoonSpinner.getSelectedItem().toString();
    }

    public String geTide() {
        return mTideSpinner.getSelectedItem().toString();
    }
}
