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
public class FishDetailsFragment extends Fragment
{
    
    private EditText mSize;
    
    private EditText mWeight;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishDetailsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fishentry_details, container, false);

        mSize = (EditText) view.findViewById(R.id.fishSizeEditText);       
        mWeight = (EditText) view.findViewById(R.id.fishWeightEditText);
        
        return view;
    }
    
    public String getWeight() {
    	return mWeight.getText().toString();
    }

    public String getSize() {
    	return mSize.getText().toString();
    }
}
