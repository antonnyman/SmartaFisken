package bros.ludumdare.smartwatch;

import java.util.ArrayList;



import com.sonymobile.smartconnect.extension.sensorsample.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FishGridAdapter extends ArrayAdapter<Fish> {
    
    private Context                 mContext;
    private ArrayList<Fish> fishes;
    private int                             layoutId;

    public FishGridAdapter(Context context, int resource,
                    ArrayList<Fish> objects) {
            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            this.mContext = context;
            this.layoutId = resource;
            this.fishes = objects;
    }

   
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(layoutId, null);
            }
           
            Fish fish = fishes.get(position);
            if(fish != null) {
                    ImageView fishImage = (ImageView) convertView.findViewById(R.id.fish_gridView_item_image);
                    TextView goodHunting = (TextView) convertView.findViewById(R.id.fish_gridview_item_goodHunting);
                    fishImage.setImageResource(fish.getFishImage());
                    goodHunting.setText(fish.getGoodHunting());
            }
            return convertView;
    }
}