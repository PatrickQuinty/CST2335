package com.example.androidlabs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<MessageClass> messages = new ArrayList<>();
    BaseAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_chatroom_relative);

        ListView msgList = findViewById(R.id.msgList);
        msgList.setAdapter(msgAdapter = new MessageAdapter());

        Button send = findViewById(R.id.sendBtn);
        Button receive = findViewById(R.id.getBtn);
        EditText msg = findViewById(R.id.msgContent);

        if(send != null)
        {
            send.setOnClickListener(
                    click -> {
                        messages.add(new MessageClass(msg.getText().toString(), true));
                        msgAdapter.notifyDataSetChanged();
                        msg.setText("");
                    }
            );
        }

        if(receive != null)
        {
            receive.setOnClickListener(
                    click -> {
                        messages.add(new MessageClass(msg.getText().toString(), false));
                        msgAdapter.notifyDataSetChanged();
                        msg.setText("");
                    }
            );
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

        public MessageClass(String message, Boolean isSend)
        {
            this.message = message;
            this.isSend = isSend;
        }

        public String getMessage() {
            return message;
        }

        public Boolean getSend() {
            return isSend;
        }
    }
}
