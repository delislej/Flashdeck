package com.ninecats.flashcard;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Cards> data;
    static View.OnLongClickListener myLongClickListener;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Cards> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.undobutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo(v);
            }
        });

        myLongClickListener = new MyLongClickListener(this);
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = loadCards();

        if (data == null) {
            data = new ArrayList<Cards>();

            for (int i = 0; i < Default.wordArray.length; i++) {
                data.add(new Cards(
                        Default.wordArray[i],
                        Default.defArray[i]
                ));
            }
        }


        removedItems = new ArrayList<Cards>();
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCards();
    }


    private static class MyLongClickListener implements View.OnLongClickListener {

        private final Context context;

        private MyLongClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onLongClick(View v) {

            removeItem(v);
            return true;
        }



        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(selectedItemPosition);
            TextView textViewName = (TextView) viewHolder.itemView.findViewById(R.id.word);
            String selectedCard = (String) textViewName.getText();
            int selectedItem = -1;
            for (int i = 0; i < data.size(); i++) {
                if (selectedCard.equals(data.get(i).def) || selectedCard.equals(data.get(i).word)) {
                    selectedItem = i;
                }
            }
            if(selectedItem!=-1) {
                removedItems.add(data.get(selectedItemPosition));
                data.remove(selectedItem);
                adapter.notifyItemRemoved(selectedItemPosition);
            }
        }
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            flipCard(v);
        }


        private void flipCard(View v){
            int selectedItemPosition = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(selectedItemPosition);
            String temp;
            if(data.get(selectedItemPosition).side == true)
            {
                temp = data.get(selectedItemPosition).getDef();
                data.get(selectedItemPosition).setSide(false);

            }
            else
            {
                temp = data.get(selectedItemPosition).getWord();
                data.get(selectedItemPosition).setSide(true);
            }
            TextView textWord = viewHolder.itemView.findViewById(R.id.word);
            textWord.setText(temp);



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void saveCards()
    {

        while(removedItems.size()!=0)
        {
            addRemovedItemToList();
        }
        SharedPreferences sharedPrefs = this.getSharedPreferences("savedCards",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //convert our card array into a json object then serialize it to the disk
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("cards", json);
        editor.commit();
    }

    public ArrayList<Cards> loadCards ()
    {
        //load in cards from sharedprefs
        SharedPreferences sharedPrefs = this.getSharedPreferences("savedCards",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("cards", null);
        Type type = new TypeToken<ArrayList<Cards>>() {}.getType();
        ArrayList<Cards> cards = gson.fromJson(json, type);
        return cards;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent indata) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                data.add(0,(Cards) indata.getSerializableExtra("card"));
                adapter.notifyItemInserted(0);
            }
            if(requestCode == 1 && resultCode == 2)
            {
                emptyTrash();
            }
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            Intent intent = new Intent(this, CardMaker.class);

            startActivityForResult(intent,1);
        }
        return true;
    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 0;
        if(removedItems.size()!=0) {
            data.add(addItemAtListPosition, removedItems.get(0));
            adapter.notifyItemInserted(addItemAtListPosition);
            removedItems.remove(0);
        }
    }

    private void emptyTrash() {
        removedItems = new ArrayList<Cards>();
    }


    public void undo(View view)
    {
        if (removedItems.size() != 0) {
            addRemovedItemToList();
        } else {
            Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
        }
    }
    }


