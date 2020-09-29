package com.example.task2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task2.Model.RepoApiModel;
import java.util.ArrayList;

public class CustomRecycleViewAdaptor extends RecyclerView.Adapter<CustomRecycleViewAdaptor.RepoBind> {
    private ArrayList<RepoApiModel> list = new ArrayList<>();
    private ArrayList<RepoApiModel> originalData = new ArrayList<>();
    private Context context;

    public CustomRecycleViewAdaptor(ArrayList<RepoApiModel> list, Context context) {
        this.list = list;
        this.originalData.addAll(list);
        this.context = context;
    }

    @NonNull
    @Override
    public RepoBind onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(this.context,R.layout.repo_recycle_view,null);
        return new RepoBind(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RepoBind holder, final int position) {

      if(this.list.get(position).getFork()==false)
        {
            holder.cardView.setCardBackgroundColor(Color.GREEN);

        }

         holder.repoName.setText("Repo Name : "+this.list.get(position).getName());
         holder.repoOwner.setText("Repo Owner : "+this.list.get(position).getOwner().getLogin());
         holder.repoDisc.setText("Repo Disc : "+this.list.get(position).getDescription());

       // when user press long time on card view
       holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {

               View view  =View.inflate(context,R.layout.customdialog,null);
               /*Dialog for Go to Repo Site or Owner Account*/
               Dialog dialog = new Dialog(context);
               dialog.setContentView(view);
               Button repo = (Button)view.findViewById(R.id.repoUrl);
               Button owner = (Button)view.findViewById(R.id.ownerUrl);
               repo.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl()));
                       context.startActivity(browserIntent);
                   }
               });

               owner.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getOwner().getUrl()));
                       context.startActivity(browserIntent);
                   }
               });
               dialog.show();
               return true;
           }
       });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    // Filter using rep Name
    public void Filter(String repo_Name)
    {
        // this is array list for store data filter result
        ArrayList<RepoApiModel> filterList = new ArrayList<>();
       if( repo_Name.isEmpty()==true||repo_Name.equals(""))
       {
          this.list.clear();
          this.list.addAll(this.originalData);
          notifyDataSetChanged();
       }else
       {
           for(int x=0;x<list.size();x++)
           {
               if(list.get(x).getName().contains(repo_Name))
               {
                   filterList.add(list.get(x));
               }
           }
         this.list.clear();
         this.list.addAll(filterList);
         notifyDataSetChanged();
   }
    }
    public class RepoBind extends RecyclerView.ViewHolder
    {
        TextView repoName , repoDisc , repoOwner;
        CardView cardView ;
        public RepoBind(@NonNull View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.repoName);
            this.repoDisc = (TextView) itemView.findViewById(R.id.repoDescription);
            this.repoOwner = (TextView) itemView.findViewById(R.id.repoOwner);
            this.cardView = (CardView)itemView.findViewById(R.id.RepoCardView);
        }
    }


}
