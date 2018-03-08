package com.example.asus.oap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private  Button mSendButton;
    private EditText mMessageEditText;
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_nickName";

    final Context context = this;
    private EditText userInput;


    private RecyclerView recyclerView;
    private List<Message> result;
    private UserAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private  String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        mAuth = FirebaseAuth.getInstance();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TEST", "Key: " + key + " Value: " + value);
            }
        }


        subscribeToPushService();

*/


        if (!loadNickName()){
            showDialog();
        }

        mSendButton = (Button) findViewById(R.id.sendButton);
        mMessageEditText= (EditText)findViewById(R.id.messageEditText);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("messages");

        //addList();

        result=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        adapter=new UserAdapter(result);
        adapter.setNickName(nickName);
        recyclerView.setAdapter(adapter);
        //addList();
        updateList();
        setmSendButtonClickListener();
        messageEditTextChangedListener();
    }
    private void createResult(){
        for (int i=0;i<10;i++){
            result.add(new Message("Metrowich","NOT result",""));
        }
    }
    private void  updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                Log.d("TEST onChildAdded",dataSnapshot.getKey());
                result.add(message);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                Log.d("TEST onChildChanged",dataSnapshot.getKey());
                int index = getItemIndex(message);
                result.set(index,message);
                adapter.notifyItemChanged(index);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                Log.d("TEST onChildRemoved",dataSnapshot.getKey());
                int index = getItemIndex(message);
                result.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addList(){
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
        reference.push().setValue(new Message("Petrowich","hello",""));
    }
    private int getItemIndex(Message message){
        int index = -1;

        for (int i=0; i< result.size(); i++){
            Log.d("TEST result.get(i).key ",result.get(i).key.toString());
            Log.d("TEST message.key ",message.key.toString());
            if (result.get(i).key.equals(message.key)){
                index=i;
                break;
            }
        }
        return  index;
    }
    public  void showDialog(){
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_signin, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        userInput = (EditText) promptsView.findViewById(R.id.input_text);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                //final_text.setText(userInput.getText());
                                saveNickName(userInput.getText().toString());
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Добро пожаловать "+userInput.getText(), Toast.LENGTH_SHORT);
                                toast.show();
                                loadNickName();
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                finish();
                                dialog.cancel();

                            }
                        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();

    }
    private void saveNickName(String nickName) {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, nickName);
        ed.commit();
        Toast.makeText(MainActivity.this, "nickName saved", Toast.LENGTH_SHORT).show();
    }
    private boolean loadNickName() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");

        nickName=savedText;
        boolean auth=false;
        if (!savedText.equals("")){
            auth=true;
            Toast.makeText(MainActivity.this, "Text loaded ="+savedText, Toast.LENGTH_SHORT).show();
        }
        return auth;
    }
    private void setmSendButtonClickListener(){
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.push().setValue(new Message(nickName,mMessageEditText.getText().toString(),""));
              /*  String topic = mMessageEditText.getText().toString();
                FirebaseMessaging.getInstance().subscribeToTopic(topic);*/
                mMessageEditText.setText("");
            }
        });
    }
    private void messageEditTextChangedListener(){
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("AndroidBash", "Subscribed");
        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("AndroidBash", token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }
}
