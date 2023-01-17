package my.edu.uitm.friendmanagement.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import my.edu.uitm.friendmanagement.models.Friend;
import my.edu.uitm.friendmanagement.models.Gender;
import my.edu.uitm.friendmanagement.models.Login;

public class FriendManagementRepository extends SQLiteOpenHelper {
    private static final String DB_NAME = "friendmanagement";
    private static final int DB_VERSION = 1;

    // login table
    private static final String TABLE_NAME_LOGIN = "login";
    private static final String ID_LOGIN_COL = "loginPK";
    private static final String PASSWORD_COL = "password";
    // friend table
    private static final String TABLE_NAME_FRIEND = "friend";
    private static final String ID_FRIEND_COL = "friendPK";
    private static final String FK_LOGIN_COL = "loginFK";
    private static final String PHOTO_COL = "photo";
    // generic columns
    private static final String NAME_COL = "name";
    private static final String GENDER_COL = "gender";
    private static final String BIRTHDATE_COL = "birthdate";
    private static final String PHONE_NO_COL = "phoneNo";
    private static final String EMAIL_COL = "email";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public FriendManagementRepository(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create login table
        String query =
                "CREATE TABLE " + TABLE_NAME_LOGIN + " ("
                        + ID_LOGIN_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + NAME_COL + " TEXT NOT NULL,"
                        + GENDER_COL + " TEXT NOT NULL,"
                        + BIRTHDATE_COL + " TEXT NOT NULL,"
                        + PHONE_NO_COL + " TEXT NOT NULL,"
                        + EMAIL_COL + " TEXT NOT NULL UNIQUE,"
                        + PASSWORD_COL + " TEXT NOT NULL)";
        db.execSQL(query);

        // create friends table
        query =
                "CREATE TABLE " + TABLE_NAME_FRIEND + " ("
                        + ID_FRIEND_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + FK_LOGIN_COL + " INTEGER UNIQUE NOT NULL,"
                        + NAME_COL + " TEXT NOT NULL,"
                        + GENDER_COL + " TEXT NOT NULL,"
                        + BIRTHDATE_COL + " TEXT NOT NULL,"
                        + PHONE_NO_COL + " TEXT NOT NULL,"
                        + EMAIL_COL + " TEXT NOT NULL,"
                        + PHOTO_COL + " TEXT,"
                        + "FOREIGN KEY(" + FK_LOGIN_COL + ") REFERENCES " + TABLE_NAME_LOGIN + "(" + ID_LOGIN_COL + "))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FRIEND);
        onCreate(db);
    }

    // CRUD methods for user table
    public long insertLogin(String name, Gender gender, Date birthdate, String phoneNo, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(GENDER_COL, gender.toString());
        values.put(BIRTHDATE_COL, birthdate.toString());
        values.put(PHONE_NO_COL, phoneNo);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, password);

        long id = db.insert(TABLE_NAME_LOGIN, null, values);

        db.close();

        return id;
    }

    @SuppressLint("Range")
    public Login getLoginByEmail(String email) {
        Login login = null;
        Cursor cursor = null;

        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME_LOGIN + " WHERE " + EMAIL_COL + "=?", new String[] {email});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                login = new Login(
                        cursor.getLong(cursor.getColumnIndex(ID_LOGIN_COL)),
                        cursor.getString(cursor.getColumnIndex(NAME_COL)),
                        Gender.valueOf(cursor.getString(cursor.getColumnIndex(GENDER_COL))),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(BIRTHDATE_COL))),
                        cursor.getString(cursor.getColumnIndex(PHONE_NO_COL)),
                        cursor.getString(cursor.getColumnIndex(EMAIL_COL)),
                        cursor.getString(cursor.getColumnIndex(PASSWORD_COL))
                );
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return login;
    }

    // CRUD methods for friend table
    public long insertFriend(long loginFK, String name, Gender gender, Date birthdate, String phoneNo, String email, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FK_LOGIN_COL, loginFK);
        values.put(NAME_COL, name);
        values.put(GENDER_COL, gender.toString());
        values.put(BIRTHDATE_COL, birthdate.toString());
        values.put(PHONE_NO_COL, phoneNo);
        values.put(EMAIL_COL, email);
        values.put(PHOTO_COL, photo);

        long id = db.insert(TABLE_NAME_LOGIN, null, values);

        db.close();

        return id;
    }

    public void updateFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FK_LOGIN_COL, friend.getLoginFK());
        values.put(NAME_COL, friend.getName());
        values.put(GENDER_COL, friend.getGender().toString());
        values.put(BIRTHDATE_COL, friend.getBirthdate().toString());
        values.put(PHONE_NO_COL, friend.getPhoneNo());
        values.put(EMAIL_COL, friend.getEmail());
        values.put(PHOTO_COL, friend.getPhoto());

        db.update(TABLE_NAME_LOGIN, values, ID_LOGIN_COL + " = ?", new String[]{friend.getId() + ""});

        db.close();
    }

    @SuppressLint("Range")
    public Friend getFriendById(long id) {
        Friend friend = null;
        Cursor cursor = null;

        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME_FRIEND + " WHERE " + ID_FRIEND_COL + "=?", new String[] {id + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                friend = new Friend(
                        cursor.getLong(cursor.getColumnIndex(ID_LOGIN_COL)),
                        cursor.getLong(cursor.getColumnIndex(FK_LOGIN_COL)),
                        cursor.getString(cursor.getColumnIndex(NAME_COL)),
                        Gender.valueOf(cursor.getString(cursor.getColumnIndex(GENDER_COL))),
                        dateFormat.parse(cursor.getString(cursor.getColumnIndex(BIRTHDATE_COL))),
                        cursor.getString(cursor.getColumnIndex(PHONE_NO_COL)),
                        cursor.getString(cursor.getColumnIndex(EMAIL_COL)),
                        cursor.getString(cursor.getColumnIndex(PHOTO_COL))
                );
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return friend;
    }

    @SuppressLint("Range")
    public List<Friend> getFriendsByFK(long fk) {
        List<Friend> friends = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME_FRIEND + " WHERE " + FK_LOGIN_COL + "=?", new String[] {fk + ""});

            if (cursor.moveToFirst()) {
                do {
                    friends.add(new Friend(
                            cursor.getLong(cursor.getColumnIndex(ID_LOGIN_COL)),
                            cursor.getLong(cursor.getColumnIndex(FK_LOGIN_COL)),
                            cursor.getString(cursor.getColumnIndex(NAME_COL)),
                            Gender.valueOf(cursor.getString(cursor.getColumnIndex(GENDER_COL))),
                            dateFormat.parse(cursor.getString(cursor.getColumnIndex(BIRTHDATE_COL))),
                            cursor.getString(cursor.getColumnIndex(PHONE_NO_COL)),
                            cursor.getString(cursor.getColumnIndex(EMAIL_COL)),
                            cursor.getString(cursor.getColumnIndex(PHOTO_COL))
                    ));
                } while(cursor.moveToNext());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return friends;
    }

    public void deleteFriendById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME_FRIEND, ID_FRIEND_COL + " = ?", new String[]{id + ""});

        db.close();
    }
}
