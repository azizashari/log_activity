package maziza.logactivity;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import maziza.logactivity.config.Config;

public class LogActivity extends AppCompatActivity {

    //deklarasi variabel
    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;
    private ProgressDialog loading;

    private TextView title;

    ListView listLog; //deklarasi list untuk menampilkan username dan aktivitas

    ArrayAdapter<String> adapter;
    private ArrayList <String> items = new ArrayList<>();

    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns; //
    private Button[] btns; //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        title = (TextView) findViewById(R.id.title);
        getData(); //masuk ke fungsi getData

        //editTextId = (EditText) findViewById(R.id.editTextId);
        //buttonGet = (Button) findViewById(R.id.buttonGet);
    }

    private void Btnfooter() {
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

        btns    =new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
                btns[i] =   new Button(this);
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setText(""+(i+1));

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                ll.addView(btns[i], lp);

                final int j = i;
                btns[j].setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v)
                    {
                        loadList(j);
                        CheckBtnBackGroud(j);
                    }
                });
            }
        }
        /**
         * Method for Checking Button Backgrounds
         */
        private void CheckBtnBackGroud(int index)
        {
            title.setText("Page "+(index+1)+" of "+noOfBtns);
            for(int i=0;i<noOfBtns;i++)
            {
                if(i==index)
                {
                    btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.box_green));
                    btns[i].setTextColor(getResources().getColor(android.R.color.white));
                }
                else
                {
                    btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btns[i].setTextColor(getResources().getColor(android.R.color.black));
                }
            }

        }

        /**
         * Method for loading data in listview
         * @param number
         */
        private void loadList(int number)
        {
            ArrayList<String> sort = new ArrayList<String>();

            int start = number * NUM_ITEMS_PAGE;
            for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
            {
                if(i<items.size())
                {
                    sort.add(items.get(i));
                }
                else
                {
                    break;
                }
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    sort);
            listLog.setAdapter(adapter);
        }

    //fungsi untuk mengambil data dari database
    private void getData() {
        //String id = editTextId.getText().toString().trim();

        /*if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.URL+ "json.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogActivity.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //menampilkan username dari tabel users dan aktivitas dari tabel log_transaksi
    private void showJSON(String response){
        listLog = (ListView) findViewById(R.id.listview);
        try {
            JSONArray result = new JSONArray(response);
            LogAktivitas[] mLogAktivitas = new LogAktivitas[result.length()];
//         Log.v("tes",""+result.length()); //untuk check
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                String a = Data.getString(Config.KEY_USERNAME);
                String b = Data.getString(Config.KEY_AKTIVITAS);
//               LogAktivitas data = new LogAktivitas("" + Data.getString(Config.KEY_USERNAME), "" + Data.getString(Config.KEY_AKTIVITAS));
//               mLogAktivitas[i] = data;
//
//               items.add("Cek:" + mLogAktivitas[i].getUsername() + mLogAktivitas[i].getAktivitas());
                items.add(a + "  " + "-" + "  " + b);
            }

            TOTAL_LIST_ITEMS = result.length();
            NUM_ITEMS_PAGE = 100; //100 data per

            Btnfooter();

            loadList(0);
            CheckBtnBackGroud(0);

        }catch(JSONException e){
            e.printStackTrace();

        }

        //parsing json
        loading.dismiss();
        /*adapter = new ArrayAdapter<String>(this, R.layout.listview,items);loading.dismiss();
        listLog.setAdapter(adapter);
        listLog.setClickable(true);*/

    }
}

