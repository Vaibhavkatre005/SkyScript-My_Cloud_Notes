package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<note, NoteAdapter.NotesViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<note> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull note nt) {
        holder.titleTextView.setText(nt.title);
        holder.contextTextView.setText(nt.content);
        holder.timeStampTextView.setText(Utility.TimeStampToString(nt.timestamp));
        holder.itemView.setOnClickListener((v)->{
            Intent intent= new Intent(context, NotesDetailsActivity.class);
            intent.putExtra("title", nt.title);
            intent.putExtra("content", nt.content);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contextTextView, timeStampTextView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.note_title_text_view);
            contextTextView=itemView.findViewById(R.id.note_content_text_view);
            timeStampTextView=itemView.findViewById(R.id.note_timestamp_text_view);

        }
    }
}
