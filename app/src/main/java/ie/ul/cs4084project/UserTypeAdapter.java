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

public class UserTypeAdapter extends ArrayAdapter<UserTypeItem> {


    public UserTypeAdapter(Context context, ArrayList<UserTypeItem> userTypeList){
        super(context, 0, userTypeList);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_user_type_row, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageViewUserType);
        TextView textView = convertView.findViewById(R.id.textViewUserType);
        UserTypeItem currentItem = getItem(position);
        if(currentItem!=null){
             imageView.setImageResource(currentItem.getUserTypeImage());
             textView.setText(currentItem.getUserType());
         }

        return convertView;
    }
}
