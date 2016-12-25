package markerlab.debra.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

import markerlab.debra.data.UsuarioContract.UsuarioEntry;


public class Usuario {
    private String id;
    private String name;
    private String direccion;
    private String phoneNumber;
    private String email;
    private String avatarUri;

    public Usuario(String name,
                   String direccion, String phoneNumber,
                   String email, String avatarUri) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.direccion = direccion;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatarUri = avatarUri;
    }

    public Usuario(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(UsuarioEntry.ID));
        name = cursor.getString(cursor.getColumnIndex(UsuarioEntry.NAME));
        direccion = cursor.getString(cursor.getColumnIndex(UsuarioEntry.DIRECCION));
        phoneNumber = cursor.getString(cursor.getColumnIndex(UsuarioEntry.PHONE_NUMBER));
        email = cursor.getString(cursor.getColumnIndex(UsuarioEntry.EMAIL));
        avatarUri = cursor.getString(cursor.getColumnIndex(UsuarioEntry.AVATAR_URI));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UsuarioEntry.ID, id);
        values.put(UsuarioEntry.NAME, name);
        values.put(UsuarioEntry.DIRECCION, direccion);
        values.put(UsuarioEntry.PHONE_NUMBER, phoneNumber);
        values.put(UsuarioEntry.EMAIL, email);
        values.put(UsuarioEntry.AVATAR_URI, avatarUri);
        return values;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUri() {
        return avatarUri;
    }
}
