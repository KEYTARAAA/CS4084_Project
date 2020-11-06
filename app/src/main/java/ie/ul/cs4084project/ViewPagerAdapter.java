package ie.ul.cs4084project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
/////////////////////////////////////
/////////////////////////////
//////////////////////////////////////
///////////////////////////////////////////
//////////////////////////////////
////////////////////////////////////////

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Integer> images;
    private List<Fragment> fragments;
    //private FragmentManager supportFragmentManager;
   /* @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
        class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            ViewPagerViewHolder(View itemView) {
                super(itemView);
            }
        }
        return new ViewPagerViewHolder(view);
    }*/

   /* @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int curImage = images.get(position);
       ImageView iv =  holder.itemView.findViewById(R.id.ivImage);
       iv.setImageResource(curImage);
    }*/

    /*@Override
    public int getItemCount() {
        return images.size();
    }*/

    public ViewPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments){
        super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        /*class ViewPagerViewHolder extends RecyclerView.ViewHolder {
            ViewPagerViewHolder(View itemView) {
                super(itemView);
            }
        }*/
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
