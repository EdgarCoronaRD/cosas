package net.ivanvega.consumircontentproviderusuario;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lsv;
    private static final int REQUEST_CODE_INSERT = 0;
    private static final int REQUEST_CODE_EDIT = 1;

    public void btnCCP_click(View v){
        consumirContenProviderUser();
        Toast.makeText(getBaseContext(),"Buscando",Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lsv = (ListView)findViewById(R.id.lsv);

    }

    private void consumirContenProviderUser() {

        Cursor c = getContentResolver().query(
            ContentProviderUsuarioContract.User.CONTENT_URI,
            new String[]{ContentProviderUsuarioContract.User.COLUM_ID,
                    ContentProviderUsuarioContract.User.COLUM_NOMBRE},
                null,null,null);
        SimpleCursorAdapter adp = new SimpleCursorAdapter(getApplicationContext(),
            R.layout.layout_item_usuario,c,
            new String[]{ContentProviderUsuarioContract.User.COLUM_ID,
                    ContentProviderUsuarioContract.User.COLUM_NOMBRE},
            new int[]{R.id.lblItemNombre,R.id.lblItemEmail},
            SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            );

        lsv.setAdapter(adp);
        lsv.setOnCreateContextMenuListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 0, 0, "Agregar Nuevo");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId()==0){
            Intent i = new Intent(getApplicationContext(), RegUsuario.class);
            i.putExtra("accion", "insert");
            startActivityForResult(i, REQUEST_CODE_INSERT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        menu.add(0, 0, 0, "Editar");
        menu.add(0,1,1,"Eliminar");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo acmi =  (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Log.d("ACMI ID", "" + acmi.id) ;
        Log.d("ACMI POSITION", "" + acmi.position) ;

        Cursor cursor  = (Cursor)lsv.getItemAtPosition(acmi.position);

        Log.d("COLUMNA ID", "" + cursor.getInt(0) ) ;
        Log.d("COLUMNA NOMBRE", "" + cursor.getString(1) ) ;

        //Editar el usuario
        if (item.getItemId()==0){
            Intent i = new Intent(this, RegUsuario.class);
            i.putExtra("accion", "edit");
            i.putExtra("id", cursor.getInt(0));
            startActivityForResult(i, REQUEST_CODE_EDIT);

        }

        //eliminar el registro
        if (item.getItemId()==1){
          int x = getContentResolver().delete(ContentProviderUsuarioContract.User.CONTENT_URI,ContentProviderUsuarioContract.User.COLUM_ID+ " = ?",new String[]{""+cursor.getInt(0)});
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode== REQUEST_CODE_INSERT){
            if (resultCode==RESULT_OK){
                consumirContenProviderUser();
            }
        }

        if(requestCode==REQUEST_CODE_EDIT){
            if(resultCode==RESULT_OK){
                consumirContenProviderUser();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
