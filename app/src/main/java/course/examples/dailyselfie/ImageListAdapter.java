package course.examples.dailyselfie;

/**
 * Created by DVTrukhin on 7/17/15.
 */
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

public class ImageListAdapter extends BaseAdapter {

    private Context mContext;
    ImageView imageView;

    private LayoutInflater mLayoutInflater;

    private ArrayList<String> mUris;

    public ImageListAdapter(Context context, ArrayList<String> uris) {
        mContext = context;
        mUris = uris;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mUris.size();
    }

    @Override
    public Object getItem(int position) {
        return mUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) mLayoutInflater.inflate(
                    R.layout.list, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        imageView = (ImageView)
                itemView.findViewById(R.id.img);


        Drawable d = Drawable.createFromPath((String)getItem(position));
        imageView.setImageDrawable(d);

        TextView txt = (TextView) itemView.findViewById(R.id.txt);

        txt.setText("Image " + (position + 1));

        return itemView;
    }

}