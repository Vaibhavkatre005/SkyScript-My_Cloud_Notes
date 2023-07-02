package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {
    EditText titleEditText, contentEditText;
     ImageButton saveNoteBtn;
     TextView pageTitleTextView, deleteNoteTextView;
     String title, content, docId;
     boolean isEditMode=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        titleEditText=findViewById(R.id.title_edit_text);
        contentEditText=findViewById(R.id.content_edit_text);
        saveNoteBtn=findViewById(R.id.save_note_btn);
        pageTitleTextView=findViewById(R.id.page_title);
        deleteNoteTextView=findViewById(R.id.delete_note_text_view_button);

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText("Edit Your note");
            deleteNoteTextView.setVisibility(View.VISIBLE);

        }


        saveNoteBtn.setOnClickListener((v)->saveNote());
        deleteNoteTextView.setOnClickListener((v)-> deleteNoteFromFirebase());

    }

    void saveNote(){
        String noteTitle=titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is Required");
            return;
        }
        note nt=new note();
        nt.setTitle(noteTitle);
        nt.setContent(noteContent);
        nt.setTimestamp(Timestamp.now());
        saveNoteInFirebase(nt);

    }
    void saveNoteInFirebase(note nt){
        DocumentReference documentReference;
        if(isEditMode){
            //update note
            documentReference =Utility.getCollectionReferenceForNote().document(docId);
        }
        else{
            //create note
            documentReference =Utility.getCollectionReferenceForNote().document();
        }

        documentReference.set(nt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes are added in firebase
                    Toast.makeText(NotesDetailsActivity.this, "Data is added in database", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    //notes are not added
                    Toast.makeText(NotesDetailsActivity.this, "Fail to add in database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        //delete note
        documentReference =Utility.getCollectionReferenceForNote().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //notes are deleted in firebase
                    Toast.makeText(NotesDetailsActivity.this, "Data is deleted in database", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    //notes are not added
                    Toast.makeText(NotesDetailsActivity.this, "Fail to add in database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}