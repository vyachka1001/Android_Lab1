package by.bsuir.rpodmp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameInfoAdapter extends RecyclerView.Adapter<GameInfoAdapter.ViewHolder> {

    private final Context context;
    private final List<UserInfo.GameInfo> gameInfoList;

    public GameInfoAdapter(Context context, List<UserInfo.GameInfo> list) {
        this.context = context;
        this.gameInfoList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (gameInfoList != null && !gameInfoList.isEmpty()) {
            UserInfo.GameInfo model = gameInfoList.get(position);
            holder.date_it.setText(Database.dateFormat.format(model.getDate()));
            holder.score_it.setText(String.valueOf(model.getScore()));
        }
    }

    @Override
    public int getItemCount() {
        return gameInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_it;
        TextView score_it;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_it = itemView.findViewById(R.id.date_it);
            score_it = itemView.findViewById(R.id.score_it);
        }
    }
}
