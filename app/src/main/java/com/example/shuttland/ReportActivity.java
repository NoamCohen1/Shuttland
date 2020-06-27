//package com.example.shuttland;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//public class ReportActivity extends AppCompatActivity {
//
//        private Client client;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_report);
//        client=new Client();
//        client.Connect();
//
//        Button empty_shuttle = (Button) findViewById(R.id.emptyShuttle);
//        empty_shuttle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                client.sendMessage(1,"empty");
//            }
//        });
//
//        Button half_shuttle = (Button) findViewById(R.id.halfFullShuttle);
//        half_shuttle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                client.sendMessage(1,"not crowded");
//            }
//        });
//
//        Button full_shuttle = (Button) findViewById(R.id.fullShuttle);
//        full_shuttle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                client.sendMessage(1,"crowded");
//            }
//        });
//    }
//
//
//
//}
