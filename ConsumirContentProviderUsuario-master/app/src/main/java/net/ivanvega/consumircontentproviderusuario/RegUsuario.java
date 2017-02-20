package net.ivanvega.consumircontentproviderusuario;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegUsuario extends Activity implements View.OnClickListener {
	EditText txtN, txtE, txtC;
	TextView lblID;
	Button btnG,btnC;
	Intent intentAccion;
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_usuario);
		
		lblID = (TextView)findViewById(R.id.lblID);
		
		txtN = (EditText)findViewById(R.id.txtNombre);
		txtE = (EditText)findViewById(R.id.txtEmail);
		txtC = (EditText)findViewById(R.id.txtPass);
		
		btnG = (Button)findViewById(R.id.btnGuardar); btnG.setOnClickListener(this);
		btnC = (Button)findViewById(R.id.btnCancelar);btnC.setOnClickListener(this);

		spinner = (Spinner) findViewById(R.id.status);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.status_usuario, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		intentAccion =  getIntent();
		Bundle datos = this.getIntent().getExtras();
		personalizarAccion(datos.getString("accion"));
	}
	
	public void personalizarAccion(String accion) {
		btnC.setText("Cancelar");
		if (accion.equals("insert")){
			this.setTitle("Registrar Usuario");
			btnG.setText("Guardar");
		}
		
		if (accion.equals("edit")){
			this.setTitle("Editar Usuario");
			btnG.setText("Actualizar");
			Bundle datos = this.getIntent().getExtras();
			int d = datos.getInt("id");
			cargarUsuario(d);
		}
	}

	private void cargarUsuario(int id) {
		Cursor cursor = (Cursor) getContentResolver().query(
				ContentProviderUsuarioContract.User.CONTENT_URI,
				new String[]{ContentProviderUsuarioContract.User.COLUM_ID,
						ContentProviderUsuarioContract.User.COLUM_NOMBRE,
						ContentProviderUsuarioContract.User.COLUM_EMAIL,
						ContentProviderUsuarioContract.User.COLUM_PASS,
						ContentProviderUsuarioContract.User.COLUM_VALIDADO},
				ContentProviderUsuarioContract.User.COLUM_ID+ " = ?",new String[]{""+id},null);
		
		try {
			if(cursor!=null){
				cursor.moveToFirst();
				lblID.setText(String.valueOf( cursor.getInt(0) ));
				txtN.setText(String.valueOf( cursor.getString(1) ));
				txtE.setText(String.valueOf( cursor.getString(2) ));
				txtC.setText(String.valueOf( cursor.getString(3) ));
			}else{
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("USUARIO", e.getMessage());
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reg_usuario, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btnG)){
			if(intentAccion.getStringExtra("accion").equals("insert")){
				insert();
			}else{
				update();
			}
		}
		
		if (v.equals(btnC)){
			finish();
		}
	}
	
	private void update() {
		ContentValues values = new ContentValues();
		values.put(ContentProviderUsuarioContract.User.COLUM_ID,Integer.parseInt( lblID.getText().toString()));
		values.put(ContentProviderUsuarioContract.User.COLUM_NOMBRE,txtN.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_EMAIL,txtE.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_PASS,txtC.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_VALIDADO,spinner.getSelectedItem().toString());
		try {
			int cc = getContentResolver().update(ContentProviderUsuarioContract.User.CONTENT_URI,values,ContentProviderUsuarioContract.User.COLUM_ID+ " = ?",new String[]{""+Integer.parseInt( lblID.getText().toString())});
			if( cc != 0){
				Toast.makeText(getBaseContext(), "USUARIO MODIFICADO",
						Toast.LENGTH_LONG).show();
				setResult(RESULT_OK, null);
				finish();
			}else {
				Toast.makeText(getBaseContext(), "FALLO AL EDITAR EL USUARIO",
						Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED, null);
				finish();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("UPDATE",e.getMessage());
			finish();
		}
	}	
	
	private void insert() {
		ContentValues values = new ContentValues();
		values.put(ContentProviderUsuarioContract.User.COLUM_NOMBRE,txtN.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_EMAIL,txtE.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_PASS,txtC.getText().toString());
		values.put(ContentProviderUsuarioContract.User.COLUM_VALIDADO,spinner.getSelectedItem().toString());
		try {
			Uri uri = getContentResolver().insert(ContentProviderUsuarioContract.User.CONTENT_URI, values);
			if(uri != null){
				Toast.makeText(getBaseContext(), " NUEVO USUARIO INSERTADO "+uri.toString(),
						Toast.LENGTH_LONG).show();
				setResult(RESULT_OK, null);
				finish();
			}else {
				Toast.makeText(getBaseContext(), "FALLO Al INSERTAR USUARIO",
						Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED, null);
				finish();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("INSERT",e.getMessage());
			finish();
		}
	}
}
