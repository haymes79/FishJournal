package au.com.mitchhaley.fishjournal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishListActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.adapter.ContactAdapter;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.ContactEntryTable;
import au.com.mitchhaley.fishjournal.db.FishEntryContentHelper;
import au.com.mitchhaley.fishjournal.db.MediaEntryContentHelper;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class ContactsFragment extends ListFragment
{

    private static final int CONTACT_PICKER_RESULT = 1001;

    private List<Contact> contacts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TripEntryActivity) getActivity()).setContactsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.generallist, container, false);

        String[] from = { "id", "name" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        this.contacts = fetchContacts();
        this.overlayExistingContacts(this.contacts);
        ContactAdapter adapter = new ContactAdapter(getActivity(), R.layout.contact_entry_row, contacts);

//        adapter = new ArrayAdapter<String>(getActivity(), R.layout.contact_entry_row, new ArrayList<String>());
        setListAdapter(adapter);

        return view;
    }

    private List<Contact> fetchContacts() {

        Cursor c =  getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        List<Contact> contacts = new ArrayList<Contact>();

        while (c.moveToNext())
        {
            Contact contact = new Contact();
            contact.setName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            contact.setContactForeign(c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));


            contacts.add(contact);
        }

        return contacts;
    }

    private void overlayExistingContacts(List<Contact> allContacts) {
        if (allContacts == null) {
            return;
        }

        Uri tripEntry = ((TripEntryActivity) getActivity()).getTripEntry();
        if (tripEntry == null || tripEntry.getLastPathSegment() == null) {
            return;
        }

        String tripId = tripEntry.getLastPathSegment();

        String[] projection = {ContactEntryTable.PRIMARY_KEY, ContactEntryTable.COLUMN_NAME_TEXT, ContactEntryTable.COLUMN_CONTACT_FK};
        Cursor c = getActivity().getContentResolver().query(TripEntryContentProvider.CONTACTS_URI, projection, ContactEntryTable.COLUMN_TRIP_ID + " = " + tripId, null, null);

        while (c.moveToNext()) {

            for (Contact contact : allContacts) {
                if (contact.getContactForeign().equalsIgnoreCase(c.getString(c.getColumnIndex(ContactEntryTable.COLUMN_CONTACT_FK)))) {
                    contact.setSelected(true);
                    contact.setId(c.getString(c.getColumnIndex(ContactEntryTable.PRIMARY_KEY)));
                }
            }
        }
    }

    public List<Contact> getSelectedContacts() {
        List<Contact> selectedContacts = new ArrayList<Contact>();

        for (Contact contact : this.contacts) {
            if (contact.isSelected())
                selectedContacts.add(contact);
        }

        return selectedContacts;
    }

    public class Contact {

        private String id;

        private String name;

        private String contactForeign;

        private boolean selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getContactForeign() {
            return contactForeign;
        }

        public void setContactForeign(String contactForeign) {
            this.contactForeign = contactForeign;
        }
    }
}
