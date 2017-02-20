package net.ivanvega.consumircontentproviderusuario;

import android.net.Uri;

/**
 * Created by SERVIDOR on 12/02/2016.
 */
public class ContentProviderUsuarioContract {
    public static class User{
        public static final Uri CONTENT_URI =

                Uri.parse("content://net.ivanvega.sqliteenandroidcurso.provider" +
                        "/usuario");
        public static final String COLUM_ID = "_id";
        public static final String COLUM_NOMBRE = "nombre";
        public static final String COLUM_EMAIL = "email";
        public static final String COLUM_PASS = "contrasenia";
        public static final String COLUM_VALIDADO = "status";

    }
}
