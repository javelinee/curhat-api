package com.project.curhatapps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, curhat;
    TextView items;
    Button send, reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getCurhat();

        send.setOnClickListener(this);
        reload.setOnClickListener(this);
    }

    void init(){
        name = findViewById(R.id.txtName);
        curhat = findViewById(R.id.txtCurhat);
        send = findViewById(R.id.buttonSend);
        reload = findViewById(R.id.buttonReload);
        items = findViewById(R.id.txtItems);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonSend){
            sendCurhat();
        } else if(v.getId() == R.id.buttonReload){
            getCurhat();
        }
    }

    void getCurhat(){
        String url ="https://coba-api.douglasnugroho.com/getCurhat.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        items.setText("");

                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String old = items.getText().toString();
                                String nama = jsonObject.getString("name");
                                String curhat = jsonObject.getString("curhat");

                                String item = nama + ":\n- " + curhat + "\n\n";
                                items.setText(old+item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    void sendCurhat(){
        String url = "https://coba-api.douglasnugroho.com/doCurhat.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String nama = name.getText().toString();
        final String curhatnya = curhat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("status");
                        Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                        curhat.setText("");

                        getCurhat();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                },
                    new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("name", nama);
                    hashMap.put("curhat", curhatnya);

                    return hashMap;
                }
        };

        requestQueue.add(stringRequest);

    }
}
