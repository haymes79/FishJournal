package au.com.mitchhaley.fishjournal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.fragment.ContactsFragment;

/**
 * Created by mitch on 15/05/14.
 */
public class ContactAdapter extends ArrayAdapter<ContactsFragment.Contact> {

    private List<ContactsFragment.Contact> contactList;

    private Context context;

    public ContactAdapter(Context context, int textViewResourceId,
                           List<ContactsFragment.Contact> contactList) {
        super(context, textViewResourceId, contactList);
        this.contactList = new ArrayList<ContactsFragment.Contact>();
        this.contactList.addAll(contactList);

        this.context = context;
    }

    private class ViewHolder {
        TextView id;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            LayoutInflater vi = LayoutInflater.from(context);
            convertView = vi.inflate(R.layout.contact_entry_row, null);

            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.contactName);
            holder.name = (CheckBox) convertView.findViewById(R.id.contactSelected);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    ContactsFragment.Contact country = (ContactsFragment.Contact) cb.getTag();
                    Toast.makeText(context,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    country.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactsFragment.Contact contact = contactList.get(position);
        holder.id.setText(" (" +  contact.getId() + ")");
        holder.name.setText(contact.getName());
        holder.name.setChecked(contact.isSelected());
        holder.name.setTag(contact);

        return convertView;

    }

}