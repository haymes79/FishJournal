package au.com.mitchhaley.fishjournal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.List;

import au.com.mitchhaley.fishjournal.R;

/**
 * Created by mitch on 11/04/14.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;

    private List<String> imageUrls;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    public void addImageUri(String imageUri) {
        imageUrls.add(imageUri);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ImageAdapter(Context context, ImageLoader imageLoader, DisplayImageOptions options, List<String> imageUrls) {
        this.context = context;
        this.imageLoader = imageLoader;
        this.options = options;
        this.imageUrls = imageUrls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(context);
            view = li.inflate(R.layout.item_grid_image, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        imageLoader.displayImage(imageUrls.get(position), holder.imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.progressBar.setProgress(0);
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current,
                                                 int total) {
                        holder.progressBar.setProgress(Math.round(100.0f * current / total));
                    }
                }
        );

        return view;
    }

    class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }
}