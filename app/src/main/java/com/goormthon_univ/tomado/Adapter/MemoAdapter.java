package com.goormthon_univ.tomado.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goormthon_univ.tomado.R;
import com.goormthon_univ.tomado.Server.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{
    Context context;
    ArrayList<Memo> items=new ArrayList<>();

    public MemoAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.recyclerview_memo,parent,false);

        return new ViewHolder(itemView,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        Memo item=items.get(position);
        holder.setItem(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Memo item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //서버 연동 객체 추가
        ServerManager server_manager;

        //유저 아이디(임시 설정)
        int user_id=35;

        Context context;

        TextView recyclerview_memo_content;
        TextView recyclerview_memo_date;
        ImageView recyclerview_memo_delete;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            recyclerview_memo_content=itemView.findViewById(R.id.recyclerview_memo_content);
            recyclerview_memo_date=itemView.findViewById(R.id.recyclerview_memo_date);
            recyclerview_memo_delete=itemView.findViewById(R.id.recyclerview_memo_delete);

            server_manager=new ServerManager(context);
            this.context=context;
        }

        public void setItem(Memo item){
            recyclerview_memo_content.setText(item.content);
            recyclerview_memo_date.setText(item.created_at);

            //삭제 버튼 클릭하면 삭제
            recyclerview_memo_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject json=new JSONObject(server_manager.http_request_delete_json("/memos?user="+user_id+"&memo="+item.id));

                        if(json.get("message").toString().equals("메모 삭제 성공")){
                            Toast.makeText(context,"메모 삭제에 성공하였습니다",Toast.LENGTH_SHORT).show();
                        }else{
                            //메모 조회 실패 시 실패 원인 보여줌
                            Toast.makeText(context,json.get("message").toString(),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
