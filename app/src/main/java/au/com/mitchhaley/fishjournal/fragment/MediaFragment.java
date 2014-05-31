package au.com.mitchhaley.fishjournal.fragment;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.adapter.ImageAdapter;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.MediaEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;

public class MediaFragment extends Fragment {

    private static final int REQUEST_TAKE_PHOTO = 1;

//    private String mCurrentPhotoPath;

    private List<String> newImages = new ArrayList<String>();

    private List<String> imagesToDisplay =  new ArrayList<String>();

    private ImageAdapter mImageAdapter;

    DisplayImageOptions options;

    private GridView mGridView;

    private File photoFile;

    private static final String TAG = "MEDIA_FRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FishEntryActivity) getActivity()).setMediaFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_media, container, false);
        setHasOptionsMenu(true);

        imagesToDisplay = new ArrayList<String>();
        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE), FishEntryContentProvider.CONTENT_ITEM_TYPE);
        }

        mGridView = (GridView) view.findViewById(R.id.imageGridView);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        mImageAdapter = new ImageAdapter(getActivity(), ImageLoader.getInstance(), options, imagesToDisplay);
        mGridView.setAdapter(mImageAdapter);

        return view;
    }

    private File getStorageDirectory() {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.canWrite()) {
            Log.e(TAG, "Cannot write to directory: " + storageDir);

            return null;
        }

        storageDir = new File(storageDir, "FISHJOURNAL");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        return storageDir;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.media_menu, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                dispatchTakePictureIntent();


                return true;
        }
    }

    private void fillData(Uri uri, String type) {
        String[] projection = new String[] { MediaEntryTable.COLUMN_MEDIA_URI };
        String[] values = new String[] {type, uri.getLastPathSegment()};

        Cursor cursor = getActivity().getContentResolver().query(TripEntryContentProvider.MEDIAS_URI, projection, MediaEntryTable.COLUMN_RELATION_TYPE + " = ? and " + MediaEntryTable.COLUMN_MEDIA_FK + " = ?", values, null);
        if (cursor != null) {

            while (cursor.moveToNext()) {
                imagesToDisplay.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaEntryTable.COLUMN_MEDIA_URI)));
            }
        }
    }


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = new File(getStorageDirectory() + "/" + imageFileName + ".jpg");

        Log.d(TAG, "Create File: " + image.getAbsolutePath());

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            File existingFile = new File(getRealPathFromURI(getActivity(), data.getData()));
            if (existingFile.exists()) {
                File newFile = createImageFile();
                if (existingFile.renameTo(newFile)) {
                    addNewPhoto(newFile);

                    String sFile = newFile.getAbsolutePath();

                    sFile = "file:///" + sFile;
                    mImageAdapter.addImageUri(sFile);
                    mImageAdapter.notifyDataSetChanged();
                }


            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean moveLastPhotoTaken(File toFile) {

        String[] projection = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE };

        final Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + "DESC");

        if (cursor != null) {
            cursor.moveToFirst();

            int column_index_data =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String image_path = cursor.getString(column_index_data);

            File file = new File(image_path);
            if (file.exists()) {
                file.renameTo(toFile);
                return true;
            }
        } 
        return false;
    }

    private void addNewPhoto(File f) {
        newImages.add("file:///" + f.getAbsolutePath());
    }

    public List<String> getNewImages() {
        return newImages;
    }
}
