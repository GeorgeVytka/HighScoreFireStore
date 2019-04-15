package com.example.triviahighscore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//make the key names constants
    public static final String QUOTE_KEY = "quote";
    public static final String AUTHOR_KEY = "author";
    public static final String TAG = "InspiringQuote";

    private EditText mQuote;
    private EditText mAuthor;
    private Button mSaveBtn;
    private TextView textQuote;
    private Button mFectchBtn;

//specify in which document to put data in.
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("simpleCollection/insiration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuote = findViewById(R.id.editQuoteText);
         mAuthor = findViewById(R.id.editAuthorText);
         mSaveBtn = findViewById(R.id.buttonSave);
         textQuote = findViewById(R.id.quoteTextView);
         mFectchBtn = findViewById(R.id.buttonFetch);


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuote();
            }
        });

        mFectchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchQuote(v);
            }
        });
    }

    public void fetchQuote(View view){
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override// document snapsot is an object that represents your document. find id etc...
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);
                    textQuote.setText("\"" + quoteText +"\" --" + authorText);
                }
            }
        });
    }



    public void saveQuote(){

        String quoteText = mQuote.getText().toString();
        String authorText = mAuthor.getText().toString();

        if(quoteText.isEmpty() || authorText.isEmpty()){ return;}

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(QUOTE_KEY, quoteText);
        dataToSave.put(AUTHOR_KEY, authorText);

        //takes the data and save it to the database
        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Document was not saved!",e);
            }
        });
    }
}
