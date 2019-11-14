package com.example.androidlabs;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<MessageClass> messages = new ArrayList<>();
    BaseAdapter msgAdapter;
    MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        db = dbOpener.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_chatroom_relative);

        ListView msgList = findViewById(R.id.msgList);
        msgList.setAdapter(msgAdapter = new MessageAdapter());

        Button send = findViewById(R.id.sendBtn);
        Button receive = findViewById(R.id.getBtn);
        EditText msg = findViewById(R.id.msgContent);



        String[] columns = {MyDatabaseOpenHelper.COL_ID,MyDatabaseOpenHelper.COL_MESSAGE,MyDatabaseOpenHelper.COL_SENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns,
                null, null, null, null, null, null);

        int idColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID),
            msgColumnIndex =results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE),
            sentColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_SENT);

        while(results.moveToNext())
        {
            long id = results.getLong(idColumnIndex);
            String message =results.getString(msgColumnIndex);
            boolean isSend = (results.getInt(sentColumnIndex)==1);
            messages.add(new MessageClass(message, isSend, id));
            msgAdapter.notifyDataSetChanged();
        }
        printCursor(results);


        if(send != null)
        {
            send.setOnClickListener(
                    click -> {
                        //add to the database and get the new ID
                        ContentValues newRowValues = new ContentValues();
                        //put string name in the NAME column:
                        String message = msg.getText().toString();
                        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
                        //put string email in the EMAIL column:
                        newRowValues.put(MyDatabaseOpenHelper.COL_SENT, true);
                        //insert in the database:
                        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

                        messages.add(new MessageClass(message, true, newId));
                        msgAdapter.notifyDataSetChanged();
                        msg.setText("");
                        }
            );
        }

        if(receive != null)
        {
            receive.setOnClickListener(
                    click -> {
                        //add to the database and get the new ID
                        ContentValues newRowValues = new ContentValues();
                        //put string name in the NAME column:
                        String message = msg.getText().toString();
                        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
                        //put string email in the EMAIL column:
                        newRowValues.put(MyDatabaseOpenHelper.COL_SENT, false);
                        //insert in the database:
                        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                        messages.add(new MessageClass(msg.getText().toString(), false, newId));
                        msgAdapter.notifyDataSetChanged();
                        msg.setText("");

                    }
            );
        }
    }


    private void printCursor(Cursor c)
    {
        Log.d("Database version:", String.valueOf(db.getVersion()));
        Log.d("Number of columns:", String.valueOf(c.getColumnCount()));
        Log.d("Name of columns:", Arrays.toString(c.getColumnNames()));
        Log.d("Number of results:", String.valueOf(c.getCount()));
        String isSent;
        c.moveToFirst();
        while(c.moveToNext())
        {
            Log.d("Id", c.getLong(c.getColumnIndex(MyDatabaseOpenHelper.COL_ID))+"");
            Log.d("Message", ""+c.getString(c.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE)));

            if(c.getInt(c.getColumnIndex(MyDatabaseOpenHelper.COL_SENT)) == 1)
                isSent = "sent";
            else
                isSent = "received";

            Log.d("Sent or received", isSent);
        }
    }


    private class MessageAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position).getMessage();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View thisRow;
            if (messages.get(position).getSend()) {
                thisRow = getLayoutInflater().inflate(R.layout.lab4_msg_send_relative, null);
            }
                else {
                thisRow = getLayoutInflater().inflate(R.layout.lab4_msg_receive_relative, null);
            }

            TextView msgText = thisRow.findViewById(R.id.msgText);
            msgText.setText(getItem(position));
            return thisRow;
        }
    }


    private class MessageClass
    {
        private String message;
        private Boolean isSend;
        private long id;

        public MessageClass(String message, Boolean isSend)
        {
            this.message = message;
            this.isSend = isSend;
        }
        public MessageClass(String message, Boolean isSend, long id)
        {
            this.message = message;
            this.isSend = isSend;
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public Boolean getSend() {
            return isSend;
        }
    }
}
