package markerlab.debra.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static markerlab.debra.data.UsuarioContract.UsuarioEntry;

/**
 * Manejador de la base de datos
 */
public class UsuarioDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Usuario.db";

    public UsuarioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UsuarioEntry.TABLE_NAME + " ("
                + UsuarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UsuarioEntry.ID + " TEXT NOT NULL,"
                + UsuarioEntry.NAME + " TEXT NOT NULL,"
                + UsuarioEntry.DIRECCION + " TEXT NOT NULL,"
                + UsuarioEntry.PHONE_NUMBER + " TEXT NOT NULL,"
                + UsuarioEntry.EMAIL + " TEXT NOT NULL,"
                + UsuarioEntry.AVATAR_URI + " TEXT,"
                + "UNIQUE (" + UsuarioEntry.ID + "))");

        // Insertar datos ficticios para prueba inicial
        mockData(db);

    }

    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockUsuario(sqLiteDatabase, new Usuario("Gregory House", "callao",
                "300 200 1111", "gregory@hotmail.com",
                "house.jpg"));
        mockUsuario(sqLiteDatabase, new Usuario("Trece Remi", "lima",
                "300 200 2222", "remi@hotmail.",
                "trece.jpg"));
        mockUsuario(sqLiteDatabase, new Usuario("China Park", "arenales",
                "300 200 3333", "park@gmail.com.",
                "park.jpg"));
        mockUsuario(sqLiteDatabase, new Usuario("Jefa Cody ", "ica",
                "300 200 4444", "codi@marina.com",
                "codi.jpg"));
        mockUsuario(sqLiteDatabase, new Usuario("Cancer Wilson", "USA",
                "300 200 5555", "wilson@hotmail.com",
                "wilson.jpg"));
        mockUsuario(sqLiteDatabase, new Usuario("Black Foreman", "Africa",
                "300 200 6666", "foreman@gmail.vcom",
                "foreman.jpg"));

    }

    public long mockUsuario(SQLiteDatabase db, Usuario usuario) {
        return db.insert(
                UsuarioEntry.TABLE_NAME,
                null,
                usuario.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long saveUsuario(Usuario usuario) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                UsuarioEntry.TABLE_NAME,
                null,
                usuario.toContentValues());

    }

    public Cursor getAllUsuario() {
        return getReadableDatabase()
                .query(
                        UsuarioEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getUsuarioById(String lawyerId) {
        Cursor c = getReadableDatabase().query(
                UsuarioEntry.TABLE_NAME,
                null,
                UsuarioEntry.ID + " LIKE ?",
                new String[]{lawyerId},
                null,
                null,
                null);
        return c;
    }

    public int deleteUsuario(String lawyerId) {
        return getWritableDatabase().delete(
                UsuarioEntry.TABLE_NAME,
                UsuarioEntry.ID + " LIKE ?",
                new String[]{lawyerId});
    }

    public int updateUsuario(Usuario usuario, String lawyerId) {
        return getWritableDatabase().update(
                UsuarioEntry.TABLE_NAME,
                usuario.toContentValues(),
                UsuarioEntry.ID + " LIKE ?",
                new String[]{lawyerId}
        );
    }
}
