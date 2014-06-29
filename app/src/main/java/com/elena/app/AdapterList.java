package com.elena.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 22/05/14.
 */

public class AdapterList extends ArrayAdapter<Event> {

    public AdapterList(Context context, int textViewResourceId,
                            Event[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.text);
            holder.owner = (TextView) convertView.findViewById(R.id.textowner);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event single = getItem(position);
        holder.title.setText(single.getTitle());
        holder.owner.setText(single.getOwner().getName());

        if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(single.getUrlImg());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView owner;
        ImageView imageView;
    }

}

class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference(imageView);
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        Bitmap bitmap=null;
        Bitmap img = null;

        try {
            if (params[0].compareTo("") != 0){
                URL urleff = null;
                try {
                    urleff = new URL(params[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 6;
                bitmap = BitmapFactory.decodeStream(urleff.openStream(), null, options);
                img = bitmap;
            }
        }catch (IOException e2){
            e2.printStackTrace();
        }
        return img;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = (ImageView) imageViewReference.get();
            if (imageView != null) {

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}