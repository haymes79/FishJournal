package au.com.mitchhaley.fishjournal.fragment;

import android.app.Dialog;
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

import java.util.Calendar;
import java.util.Date;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class FishDetailsFragment extends Fragment
{
    
    private EditText mSize;
    
    private EditText mWeight;

    private EditText mDateTime;

    private DateTimePicker custom;

    private Date dateTime;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishDetailsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fishentry_details, container, false);

        mSize = (EditText) view.findViewById(R.id.fishSizeEditText);       
        mWeight = (EditText) view.findViewById(R.id.fishWeightEditText);

        mDateTime = ((EditText) view.findViewById(R.id.dateTimeEdit));
        mDateTime.setText(DateTimePicker.convertDate(new Date(), "dd/MM/yyyy hh:mm"));

        custom = new DateTimePicker(getActivity(),
                new DateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
//                        ((EditText) view.findViewById(R.id.dateTimeEdit)).setText(calendarSelected.get(Calendar.DAY_OF_MONTH) + "/" + (monthNumber + 1) + "/" + year + ", " + hour12 + ":" + min + " " + AM_PM);
                        dateTime = dateSelected;
                        mDateTime.setText(DateTimePicker.convertDate(dateSelected, "dd/MM/yyyy hh:mm"));
                    }

                    @Override
                    public void onCancel() {

                    }
                });

          custom.set24HourFormat(false);
          custom.setDate(Calendar.getInstance());

        view.findViewById(R.id.dateTimeButton).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        return view;
    }
    
    public String getWeight() {
    	return mWeight.getText().toString();
    }

    public String getSize() {
    	return mSize.getText().toString();
    }

    public long getSelectedDateTime() {
        return dateTime.getTime();
    }
}
