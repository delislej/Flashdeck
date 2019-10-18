package com.ninecats.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardMaker extends AppCompatActivity {

    EditText editText;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_maker);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCard();
            }
        });

        Button button2 = findViewById(R.id.emptyTrash);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(2,intent);}
        });
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);

    }
    public void makeCard()
    {
        Cards card = new Cards(editText.getText().toString(),editText2.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("card",card);
        setResult(RESULT_OK,intent);

    }


}
