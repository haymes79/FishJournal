package au.com.mitchhaley.fishjournal.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class TripDetailsFragment extends Fragment
{
    
    private EditText mTitle;

    private EditText mStartDateEdit;

    private DateTimePicker startDatePicker;

    private Date startDate;

    private EditText mEndDateEdit;

    private DateTimePicker endDatePicker;

    private Date endDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TripEntryActivity) getActivity()).setTripDetailsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tripentry_details, container, false);

        mTitle = (EditText) view.findViewById(R.id.tripTitleEditText);

        mStartDateEdit = ((EditText) view.findViewById(R.id.startDateEdit));
        mEndDateEdit = ((EditText) view.findViewById(R.id.endDateEdit));

        if (getArguments() != null && getArguments().containsKey(TripEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(TripEntryContentProvider.CONTENT_ITEM_TYPE));
        } else {
            startDate = new Date();
            mStartDateEdit.setText(DateTimeHelper.convertDate(startDate, DateTimeHelper.dateTimeDisplayFormat));

            endDate = new Date();
            mEndDateEdit.setText(DateTimeHelper.convertDate(endDate, DateTimeHelper.dateTimeDisplayFormat));

        }


        startDatePicker = new DateTimePicker(getActivity(),
                new DateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        startDate = dateSelected;
                        mStartDateEdit.setText(DateTimeHelper.convertDate(dateSelected, DateTimeHelper.dateTimeDisplayFormat));
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        startDatePicker.set24HourFormat(false);
        startDatePicker.setDate(Calendar.getInstance());

        view.findViewById(R.id.startDateButton).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startDatePicker.showDialog();
                    }
                });

        endDatePicker = new DateTimePicker(getActivity(),
                new DateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        endDate = dateSelected;
                        mEndDateEdit.setText(DateTimeHelper.convertDate(dateSelected, DateTimeHelper.dateTimeDisplayFormat));
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        endDatePicker.set24HourFormat(false);
        endDatePicker.setDate(Calendar.getInstance());

        view.findViewById(R.id.endDateButton).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        endDatePicker.showDialog();
                    }
                });

        return view;
    }

    private void fillData(Uri uri) {
        String[] projection = new String[] { TripEntryTable.COLUMN_TITLE, TripEntryTable.COLUMN_START_DATETIME, TripEntryTable.COLUMN_END_DATETIME };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_TITLE)));

            long startDateTimeLong = cursor.getLong(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_START_DATETIME));
            startDate = new Date(startDateTimeLong);
            mStartDateEdit.setText(DateTimeHelper.convertDate(startDate, DateTimeHelper.dateTimeDisplayFormat));

            long endDateTimeLong = cursor.getLong(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_END_DATETIME));
            endDate = new Date(endDateTimeLong);
            mEndDateEdit.setText(DateTimeHelper.convertDate(endDate, DateTimeHelper.dateTimeDisplayFormat));


            // always close the cursor
            cursor.close();
        }
    }
    
    public String getTitle() {
    	return mTitle.getText().toString();
    }

    public long getSelectedStartDateTime() {
        return startDate.getTime();
    }

    public long getSelectedEndDateTime() {
        return endDate.getTime();
    }


}
