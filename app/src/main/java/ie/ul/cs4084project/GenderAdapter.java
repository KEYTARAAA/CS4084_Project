package ie.ul.cs4084project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GenderAdapter extends ArrayAdapter<GenderItem> {


    public GenderAdapter(Context context, ArrayList<GenderItem> genderList){
        super(context, 0, genderList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_gender_row, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewGender);
        TextView textView = convertView.findViewById(R.id.textViewGender);
        GenderItem currentItem = getItem(position);
        if(currentItem!=null){
             imageView.setImageResource(currentItem.getGenderImage());
             textView.setText(currentItem.getGender());
         }

        return convertView;
    }
}
