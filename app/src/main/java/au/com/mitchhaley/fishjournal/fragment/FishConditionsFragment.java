package au.com.mitchhaley.fishjournal.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.Arrays;
import java.util.Date;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.adapter.ImageSpinnerAdapter;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;

/**
 * Created by mitch on 18/10/13.
 */
public class FishConditionsFragment extends Fragment
{
    private static final String[] tideValues = {"NA", "High", "Low"};
    
    private static final String[] moonValues = {"NA", "New Moon", "First Quarter", "Full Moon", "Last Quarter"};

    public static final String[] conditionValues = {"Sunny", "Cloudy", "Storms", "Light Rain", "Snow"};

    private static final int[] moonImages = {-1, R.drawable.new_moon, R.drawable.first_quarter_moon, R.drawable.full_moon, R.drawable.last_quarter_moon};

    public static final int[] conditionImages = {R.drawable.sunny, R.drawable.cloudy, R.drawable.tstorm, R.drawable.light_rain, R.drawable.snow};

    private Spinner mConditionSpinner;

    private Spinner mMoonSpinner;

    private Spinner mTideSpinner;
    
    private TextView mTemperatureSeekBarValue;

    private TextView mWaterDepthTextValue;

    private SeekBar mTemperatureSeekBar;

    private TextView mWaterTemperatureSeekBarValue;

    private SeekBar mWaterTemperatureSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishConditionsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fishentry_conditions, container, false);

        mTemperatureSeekBar = (SeekBar) view.findViewById(R.id.seekBarTemperature);
        mTemperatureSeekBar.setMax(70);
        mTemperatureSeekBarValue = (TextView) view.findViewById(R.id.temperatureValue);
        mTemperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            	mTemperatureSeekBarValue.setText(String.valueOf(progress - 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mWaterTemperatureSeekBar = (SeekBar) view.findViewById(R.id.seekBarTemperature);
        mWaterTemperatureSeekBar.setMax(70);
        mWaterTemperatureSeekBarValue = (TextView) view.findViewById(R.id.temperatureValue);
        mWaterTemperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mWaterTemperatureSeekBarValue.setText(String.valueOf(progress - 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mConditionSpinner = (Spinner) view.findViewById(R.id.spinnerConditions);
        ImageSpinnerAdapter conditionsAdapter = new ImageSpinnerAdapter(getActivity(), R.layout.image_spinner_row, conditionValues);
        conditionsAdapter.setImages(conditionImages);
        mConditionSpinner.setAdapter(conditionsAdapter);

        mTideSpinner = (Spinner) view.findViewById(R.id.tideSpinner);
        mTideSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_entry_row, tideValues));

        mMoonSpinner = (Spinner) view.findViewById(R.id.spinnerMoon);
        ImageSpinnerAdapter moonAdapter = new ImageSpinnerAdapter(getActivity(), R.layout.image_spinner_row, moonValues);
        moonAdapter.setImages(moonImages);
        mMoonSpinner.setAdapter(moonAdapter);

        mWaterDepthTextValue = (TextView) view.findViewById(R.id.waterDepth);

        if (((FishEntryActivity) getActivity()).getFishEntry() != null) {
            fillData(((FishEntryActivity) getActivity()).getFishEntry());
        } else {
            mTemperatureSeekBar.setProgress(Integer.parseInt(mTemperatureSeekBarValue.getText().toString()) + 20);
            mWaterTemperatureSeekBar.setProgress(Integer.parseInt(mWaterTemperatureSeekBarValue.getText().toString()) + 20);
        }

        return view;
    }

    private void fillData(Uri uri) {
        String[] projection = new String[] { FishEntryTable.COLUMN_CONDITIONS, FishEntryTable.COLUMN_TEMPERATURE, FishEntryTable.COLUMN_WATER_TEMPERATURE, FishEntryTable.COLUMN_MOON, FishEntryTable.COLUMN_MOON, FishEntryTable.COLUMN_WATER_DEPTH, FishEntryTable.COLUMN_TIDE };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            String condition = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_CONDITIONS));
            mConditionSpinner.setSelection(Arrays.asList(conditionValues).indexOf(condition));

            String tide = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_TIDE));
            mTideSpinner.setSelection(Arrays.asList(tideValues).indexOf(tide));

            String moon = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_MOON));
            mMoonSpinner.setSelection(Arrays.asList(moonValues).indexOf(moon));

            String temperature = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_TEMPERATURE));
            mTemperatureSeekBar.setProgress(Integer.parseInt(temperature) + 20);

            String waterTemperature = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_WATER_TEMPERATURE));
            mWaterTemperatureSeekBar.setProgress(Integer.parseInt(temperature) + 20);

            mWaterDepthTextValue.setText(cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_WATER_DEPTH)));
        }
    }
    
    public String getCondition() {
    	return mConditionSpinner.getSelectedItem().toString();
    }

    public String getWaterTemperature() {
        return mWaterTemperatureSeekBarValue.getText().toString();
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

    public String getWaterDepth() {
        return mWaterDepthTextValue.getText().toString();
    }
}
