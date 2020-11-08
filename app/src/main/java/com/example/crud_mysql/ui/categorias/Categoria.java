package com.example.crud_mysql.ui.categorias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.crud_mysql.MySingleton;
import com.example.crud_mysql.R;
import com.example.crud_mysql.Setting_VAR;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Categoria extends Fragment  implements View.OnClickListener{

    private TextInputLayout ti_idcategoria,ti_namecategoria;
    private EditText et_id_categoria, et_namecategoria;
    private Spinner sp_estado;
    private Button btn_Save, btnNew, btnVer;
    ListView listaResultado;
    private TextView textView;


    String datoSelect="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categoria, container, false);

        listaResultado = root.findViewById(R.id.lvLista);

        textView = root.findViewById(R.id.text_home);

        ti_idcategoria = root.findViewById(R.id.ti_idcategoria);
        ti_namecategoria = root.findViewById(R.id.ti_namecategoria);

        et_id_categoria = root.findViewById(R.id.et_idcategoria);
        et_namecategoria = root.findViewById(R.id.et_namecategoria);
        sp_estado = root.findViewById(R.id.sp_estado);


   /* btnVer = root.findViewById(R.id.btnVer);
     btnVer.setOnClickListener(new View.OnClickListener() {
      @Override
   public void onClick(View v) {


      Intent lista = new Intent(getContext(), Ojala.class);
  startActivity(lista);

    }
  });







*/





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.estadoCategorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapter);

        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_estado.getSelectedItemPosition()>0){
                    datoSelect = sp_estado.getSelectedItem().toString();

                }else{
                    datoSelect="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
        btn_Save = root.findViewById(R.id.btnSave);
        btnNew= root.findViewById(R.id.btnNew);
        btnVer= root.findViewById(R.id.btnVer);

        btn_Save.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnVer.setOnClickListener(this);








        return root;



    }










    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSave:

                String id = et_id_categoria.getText().toString();
                String nombre = et_namecategoria.getText().toString();
                if (id.length()==0){
                    et_id_categoria.setError("Campo Obligatorio");
                }else  if (nombre.length()==0){
                    et_namecategoria.setError("Campo Obligatorio");


                }else  if (sp_estado.getSelectedItemPosition()>0){
                    Toast.makeText(getContext(), "Bien.....", Toast.LENGTH_SHORT).show();
                    save_server(getContext(), Integer.parseInt(id), nombre, Integer.parseInt(datoSelect));

                }else {
                    Toast.makeText(getContext(), "Debe Seleccionar un estado para la categoria", Toast.LENGTH_SHORT).show();

                }
                break;


            case  R.id.btnNew:
                new_categories();
                break;

            case  R.id.btnVer:
            Intent lista = new Intent(getContext(), Ojala.class);
                startActivity(lista);
                break;

            default:
        }
    }





    private  void save_server(final Context context, final  int id_categoria, final String nom_categoria, final int estado_categoria){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.URL_guardar_categoria, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if (estado.equals("1")) {
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    } else if (estado.equals("2")) {
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo guardar. \n "+ "Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams()throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(id_categoria));
                map.put("nombre", nom_categoria);
                map.put("estado", String.valueOf(estado_categoria));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private  void new_categories(){
        et_id_categoria.setText(null);
        et_namecategoria.setText(null);
        sp_estado.setSelection(0);
    }




}

