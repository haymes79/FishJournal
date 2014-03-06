package au.com.mitchhaley.fishjournal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    
    private final String[] moonValues = {"NA", "New Moon", "First Quarter", "Full Moon", "Last Quarter"};

    private final int[] moonImages = {R.drawable.sunny, R.drawable.cloudy, R.drawable.tstorm, R.drawable.light_rain, R.drawable.snow};

    private final String[] conditionValues = {"Sunny", "Cloudy", "Storms", "Light Rain", "Snow"};

    private final int[] conditionImages = {R.drawable.sunny, R.drawable.cloudy, R.drawable.tstorm, R.drawable.light_rain, R.drawable.snow};

    private Spinner mConditionSpinner;
    
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
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        mConditionSpinner = (Spinner) view.findViewById(R.id.spinnerConditions);
        mConditionSpinner.setAdapter(new MyAdapter(getActivity(), R.layout.conditions_spinner_row, conditionValues));
        
        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
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
            View row=inflater.inflate(R.layout.conditions_spinner_row, parent, false);
            TextView label=(TextView) row.findViewById(R.id.conditionTextValue);
            label.setText(conditionValues[position]);

            ImageView icon=(ImageView) row.findViewById(R.id.conditionImage);
            icon.setImageResource(conditionImages[position]);

            return row;
        }
    }
    
    public String getCondition() {
    	return mConditionSpinner.getSelectedItem().toString();
    }
    
    public String getTemperature() {
    	return mTemperatureSeekBarValue.getText().toString();
    }    
}
