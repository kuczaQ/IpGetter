package com.adam.ipgetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ipgetter.adam.com.ipgetter.R;

public class MainActivity extends AppCompatActivity {
    final static private String DATABASE_URL  = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7260160",
                                DATABASE_USER = "sql7260160";
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Load JDBC driver
        } catch (ClassNotFoundException e) {
            Log.e(MainActivity.class.getName(), e.getMessage(), e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView ip = findViewById(R.id.ipText);
        new Thread() {
            @Override
            public void run() {
                final String ipText = getIPFromDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ip.setText(ipText);
                    }
                });
            }
        }.start();

    }

    private static String getIPFromDB() {
        try (Statement stmt = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, "").createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT ip_address FROM ip_uploader WHERE entry_id=1;");
            resultSet.next();
            return resultSet.getString("ip_address");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot post IP!", e);
        }
    }
}
