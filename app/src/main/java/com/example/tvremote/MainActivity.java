package com.example.tvremote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText etTvIp;
    private static final int SERVER_PORT = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTvIp = findViewById(R.id.etTvIp);
        Button btnVolUp = findViewById(R.id.btnVolUp);
        Button btnVolDown = findViewById(R.id.btnVolDown);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnBack = findViewById(R.id.btnBack);

        btnVolUp.setOnClickListener(v -> sendCommand("CMD_VOL_UP"));
        btnVolDown.setOnClickListener(v -> sendCommand("CMD_VOL_DOWN"));
        btnHome.setOnClickListener(v -> sendCommand("CMD_HOME"));
        btnBack.setOnClickListener(v -> sendCommand("CMD_BACK"));
    }

    private void sendCommand(String command) {
        String ip = etTvIp.getText().toString().trim();
        if (ip.isEmpty()) {
            Toast.makeText(this, "يرجى كتابة عنوان IP الشاشة أولاً", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, SERVER_PORT), 1500);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(command);
                out.flush();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
