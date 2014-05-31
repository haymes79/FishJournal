package au.com.mitchhaley.fishjournal.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.ContactsFragment;

public class ContactContentHelper {

	public static void create(Context context, List<ContactsFragment.Contact> contacts, String tripId) {

        if (contacts == null) {
            return;
        }

        for (ContactsFragment.Contact contact : contacts) {

            ContentValues values = new ContentValues();
            values.put(ContactEntryTable.COLUMN_TRIP_ID, tripId);
            values.put(ContactEntryTable.COLUMN_CONTACT_FK, contact.getContactForeign());
            values.put(ContactEntryTable.COLUMN_NAME_TEXT, contact.getName());

            context.getContentResolver().insert(TripEntryContentProvider.CONTACTS_URI, values);
        }
	}
	
}

