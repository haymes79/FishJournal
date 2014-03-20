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

    private SeekBar mSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishConditionsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fishentry_conditions, container, false);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekBarTemperature);
        mSeekBar.setMax(70);
        mTemperatureSeekBarValue = (TextView) view.findViewById(R.id.temperatureValue);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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

        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE));
        }

        return view;
    }

    private void fillData(Uri uri) {
        String[] projection = new String[] { FishEntryTable.COLUMN_CONDITIONS, FishEntryTable.COLUMN_TEMPERATURE };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            String condition = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_CONDITIONS));
            mConditionSpinner.setSelection(Arrays.asList(conditionValues).indexOf(condition));

            String temperature = cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_TEMPERATURE));
            mSeekBar.setProgress(Integer.parseInt(temperature) + 20);

            // always close the cursor
            cursor.close();
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
