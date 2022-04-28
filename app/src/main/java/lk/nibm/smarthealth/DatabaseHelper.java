package lk.nibm.smarthealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import lk.nibm.smarthealth.DatabaseHelperContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "SmartHealth.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + users.TABLE_NAME +
                " (" + users.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                users.COLUMN_FIRSTNAME + " TEXT, " +
                users.COLUMN_LASTNAME + " TEXT, " +
                users.COLUMN_EMAIL + " TEXT, " +
                users.COLUMN_PASSWORD + " TEXT, " +
                users.COLUMN_WATERINFO + " INTEGER, " +
                users.COLUMN_SLEEPSTARTED + " INTEGER, " +
                users.COLUMN_SLEEPPAUSED + " INTEGER" + ");";

        db.execSQL(createUsers);

        String createBmi = "CREATE TABLE " + bmi.TABLE_NAME +
                " (" + bmi.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    bmi.COLUMN_USERID + " INTEGER, " +
                bmi.COLUMN_DATE + " DATE, " +
                bmi.COLUMN_HEIGHT + " FLOAT, " +
                bmi.COLUMN_WEIGHT + " FLOAT, " +
                bmi.COLUMN_BMI + " FLOAT, " +
                "FOREIGN KEY (" + bmi.COLUMN_USERID + ") " +
                "REFERENCES " + users.TABLE_NAME + "(" + users.COLUMN_ID + ")" + " )";

        db.execSQL(createBmi);

        String createNotes = "CREATE TABLE " + notes.TABLE_NAME +
                " (" + notes.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                notes.COLUMN_USERID + " INTEGER, " +
                notes.COLUMN_TITLE + " TEXT, " +
                notes.COLUMN_CATEGORY + " TEXT, " +
                notes.COLUMN_NOTE + " TEXT, " +
                notes.COLUMN_DATE + " DATE, " +
                notes.COLUMN_TIME + " TIME, " +
                "FOREIGN KEY (" + notes.COLUMN_USERID + ") " +
                "REFERENCES " + users.TABLE_NAME + "(" + users.COLUMN_ID + ")" + " )";

        db.execSQL(createNotes);

        String createWater = "CREATE TABLE " + water.TABLE_NAME +
                " (" + water.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                water.COLUMN_USERID + " INTEGER, " +
                water.COLUMN_DATE + " DATE, " +
                water.COLUMN_CAPACITY + " INTEGER, " +
                water.COLUMN_CUP + " INTEGER, " +
                water.COLUMN_CURRENT + " INTEGER, " +
                water.COLUMN_PERCENTAGE + " INTEGER, " +
                "FOREIGN KEY (" + water.COLUMN_USERID + ") " +
                "REFERENCES " + users.TABLE_NAME + "(" + users.COLUMN_ID + ")" + " )";

        db.execSQL(createWater);

        String createSleep = "CREATE TABLE " + sleep.TABLE_NAME +
                " (" + sleep.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                sleep.COLUMN_USERID + " INTEGER, " +
                sleep.COLUMN_DATE + " DATE, " +
                sleep.COLUMN_STARTED + " TIME, " +
                sleep.COLUMN_STOPPED + " TIME, " +
                sleep.COLUMN_TOTALSLEPT + " TIME, " +
                sleep.COLUMN_TOTALPAUSED + " TIME, " +
                sleep.COLUMN_TIMESSLEPT + " INTEGER, " +
                "FOREIGN KEY (" + sleep.COLUMN_USERID + ") " +
                "REFERENCES " + users.TABLE_NAME + "(" + users.COLUMN_ID + ")" + " )";

        db.execSQL(createSleep);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + users.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + bmi.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + notes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + water.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + sleep.TABLE_NAME);

        onCreate(db);
    }

    void signUp(String firstname, String lastname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(users.COLUMN_FIRSTNAME, firstname);
        cv.put(users.COLUMN_LASTNAME, lastname);
        cv.put(users.COLUMN_EMAIL, email);
        cv.put(users.COLUMN_PASSWORD, password);
        cv.put(users.COLUMN_WATERINFO, 0);
        cv.put(users.COLUMN_SLEEPSTARTED, 0);
        cv.put(users.COLUMN_SLEEPPAUSED, 0);

        long result = db.insert(users.TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Sign up success!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor signIn(String email) {
        String query = "SELECT " + users.COLUMN_ID + ", " +
                users.COLUMN_EMAIL + ", " +
                users.COLUMN_PASSWORD + " FROM " +
                users.TABLE_NAME + " WHERE " +
                users.COLUMN_EMAIL + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ email });
        }

        return cursor;
    }

    Cursor checkExistingEmail(String email) {
        String query = "SELECT " + users.COLUMN_EMAIL +
                " FROM " +
                users.TABLE_NAME + " WHERE " +
                users.COLUMN_EMAIL + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ email });
        }

        return cursor;
    }

    Cursor checkBmiToday(String date, int id) {
        String query = "SELECT " + bmi.COLUMN_DATE + ", " +
                bmi.COLUMN_HEIGHT + ", " +
                bmi.COLUMN_WEIGHT +
                " FROM " +
                bmi.TABLE_NAME + " WHERE " +
                bmi.COLUMN_DATE + " = ? AND " +
                bmi.COLUMN_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ date , String.valueOf(id)});
        }

        return cursor;
    }

    void saveBmi (int userid, String date, Float height, Float weight, Float bmi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelperContract.bmi.COLUMN_USERID, userid);
        cv.put(DatabaseHelperContract.bmi.COLUMN_DATE, date);
        cv.put(DatabaseHelperContract.bmi.COLUMN_HEIGHT, height);
        cv.put(DatabaseHelperContract.bmi.COLUMN_WEIGHT, weight);
        cv.put(DatabaseHelperContract.bmi.COLUMN_BMI, bmi);

        long result = db.insert(DatabaseHelperContract.bmi.TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"BMI saved!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateBmi (int userid, String date, Float height, Float weight, Float bminew) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(bmi.COLUMN_HEIGHT, height);
        cv.put(bmi.COLUMN_WEIGHT, weight);
        cv.put(bmi.COLUMN_BMI, bminew);

        long result = db.update(bmi.TABLE_NAME, cv,
                bmi.COLUMN_USERID + " = ? AND " + bmi.COLUMN_DATE + " = ?",
                new String[] {String.valueOf(userid), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"BMI updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteBmiHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(bmi.TABLE_NAME, bmi.COLUMN_USERID + " = ?", new String[] {String.valueOf(id)});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"BMI history deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor bmiHistory(int id) {
        String query = "SELECT " + bmi.COLUMN_DATE + ", " +
                bmi.COLUMN_HEIGHT + ", " +
                bmi.COLUMN_WEIGHT + ", " +
                bmi.COLUMN_BMI + " FROM " +
                bmi.TABLE_NAME + " WHERE " +
                bmi.COLUMN_USERID + " = ? " +
                "ORDER BY " + bmi.COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(id) });
        }

        return cursor;
    }

    void saveNote(int id, String title, String category, String note, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(notes.COLUMN_USERID, id);
        cv.put(notes.COLUMN_TITLE, title);
        cv.put(notes.COLUMN_CATEGORY, category);
        cv.put(notes.COLUMN_NOTE, note);
        cv.put(notes.COLUMN_DATE, date);
        cv.put(notes.COLUMN_TIME, time);

        long result = db.insert(notes.TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Note saved!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateNote(int id, String title, String category, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(notes.COLUMN_TITLE, title);
        cv.put(notes.COLUMN_CATEGORY, category);
        cv.put(notes.COLUMN_NOTE, note);

        long result = db.update(notes.TABLE_NAME, cv, notes.COLUMN_ID + " = ?", new String[] {String.valueOf(id)});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Note updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllMedicalNotes(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(notes.TABLE_NAME, notes.COLUMN_USERID + " = ?", new String[] {String.valueOf(id)});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"All notes deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteMedicalNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(notes.TABLE_NAME, notes.COLUMN_ID + " = ?", new String[] {String.valueOf(id)});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Note deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor displayNotes(int id) {
        String query = "SELECT " + notes.COLUMN_ID + ", " +
                notes.COLUMN_TITLE + ", " +
                notes.COLUMN_CATEGORY + ", " +
                notes.COLUMN_NOTE + ", " +
                notes.COLUMN_DATE + ", " +
                notes.COLUMN_TIME + " FROM " +
                notes.TABLE_NAME + " WHERE " +
                notes.COLUMN_USERID + " = ? " +
                "ORDER BY " + notes.COLUMN_DATE + " DESC, " +
                notes.COLUMN_TIME + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(id) });
        }

        return cursor;
    }

    Cursor checkWaterInfo(int userID) {
        String query = "SELECT " + users.COLUMN_WATERINFO +
                " FROM " +
                users.TABLE_NAME + " WHERE " +
                users.COLUMN_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID) });
        }

        return cursor;
    }

    Cursor checkWaterPercentage(int userID, String date) {
        String query = "SELECT " + water.COLUMN_PERCENTAGE + ", " +
                water.COLUMN_CAPACITY + ", " +
                water.COLUMN_CUP + ", " +
                water.COLUMN_CURRENT +
                " FROM " +
                water.TABLE_NAME + " WHERE " +
                water.COLUMN_USERID + " = ? AND " +
                water.COLUMN_DATE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID), date });
        }

        return cursor;
    }

    void updateWaterInfo(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(users.COLUMN_WATERINFO, 1);

        long result = db.update(users.TABLE_NAME, cv, users.COLUMN_ID + " = ?", new String[] {String.valueOf(userID)});

        if (result == -1) {
            Toast.makeText(context,"Database Error while updating status!", Toast.LENGTH_SHORT).show();
        }
    }

    void saveWaterCapacity(int userID, String date, int waterCapacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(water.COLUMN_USERID, userID);
        cv.put(water.COLUMN_DATE, date);
        cv.put(water.COLUMN_CAPACITY, waterCapacity);
        cv.put(water.COLUMN_CUP, 0);
        cv.put(water.COLUMN_CURRENT, 0);
        cv.put(water.COLUMN_PERCENTAGE, 0);

        long result = db.insert(water.TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Water capacity saved!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateWaterCapacity(int userID, String date, int waterCapacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(water.COLUMN_CAPACITY, waterCapacity);

        long result = db.update(water.TABLE_NAME, cv, water.COLUMN_USERID + " = ? AND " + water.COLUMN_DATE + " = ?", new String[] {String.valueOf(userID), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Water capacity updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void addWater(int userID, String date, int waterCup, int waterCurrent, int waterPercentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(water.COLUMN_CUP, waterCup);
        cv.put(water.COLUMN_CURRENT, waterCurrent);
        cv.put(water.COLUMN_PERCENTAGE, waterPercentage);

        long result = db.update(water.TABLE_NAME, cv, water.COLUMN_USERID + " = ? AND " + water.COLUMN_DATE + " = ?", new String[] {String.valueOf(userID), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Water cup added!", Toast.LENGTH_SHORT).show();
        }
    }

    void removeWater(int userID, String date, int waterCup, int waterCurrent, int waterPercentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(water.COLUMN_CUP, waterCup);
        cv.put(water.COLUMN_CURRENT, waterCurrent);
        cv.put(water.COLUMN_PERCENTAGE, waterPercentage);

        long result = db.update(water.TABLE_NAME, cv, water.COLUMN_USERID + " = ? AND " + water.COLUMN_DATE + " = ?", new String[] {String.valueOf(userID), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Water cup removed!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor checkWaterHistory(int userID) {
        String query = "SELECT " + water.COLUMN_DATE + ", " +
                water.COLUMN_CURRENT +
                " FROM " +
                water.TABLE_NAME + " WHERE " +
                water.COLUMN_USERID + " = ?" +
                "ORDER BY " + water.COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID) });
        }

        return cursor;
    }

    Cursor checkSleepStarted(int userID) {
        String query = "SELECT " + users.COLUMN_SLEEPSTARTED +
                " FROM " +
                users.TABLE_NAME + " WHERE " +
                users.COLUMN_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID) });
        }

        return cursor;
    }

    Cursor checkSleepStartedTime(int userID, String date) {
        String query = "SELECT " + sleep.COLUMN_STARTED +
                " FROM " +
                sleep.TABLE_NAME + " WHERE " +
                sleep.COLUMN_USERID + " = ? AND " +
                sleep.COLUMN_DATE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID), date });
        }

        return cursor;
    }

    void updateSleepStarted(int userID, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(users.COLUMN_SLEEPSTARTED, status);

        long result = db.update(users.TABLE_NAME, cv, users.COLUMN_ID + " = ?", new String[] {String.valueOf(userID)});

        if (result == -1) {
            Toast.makeText(context, "Database Error!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor checkTimesSlept(int userID, String date) {
        String query = "SELECT " + sleep.COLUMN_TIMESSLEPT +
                " FROM " +
                sleep.TABLE_NAME + " WHERE " +
                sleep.COLUMN_USERID + " = ? AND " +
                sleep.COLUMN_DATE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID), date });
        }

        return cursor;
    }

    Cursor checkSavedSleepToday(int userID, String date) {
        String query = "SELECT " + sleep.COLUMN_ID +
                " FROM " +
                sleep.TABLE_NAME + " WHERE " +
                sleep.COLUMN_USERID + " = ? AND " +
                sleep.COLUMN_DATE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{ String.valueOf(userID), date });
        }

        return cursor;
    }

    void startSleep(int userID, String date, String started, int timesSlept) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(sleep.COLUMN_USERID, userID);
        cv.put(sleep.COLUMN_DATE, date);
        cv.put(sleep.COLUMN_STARTED, started);
        cv.put(sleep.COLUMN_TIMESSLEPT, timesSlept);

        long result = db.insert(sleep.TABLE_NAME,null, cv);

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Sleep timer started!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateSleepStart(int userID, String date, String started, int timesSlept) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(sleep.COLUMN_STARTED, started);
        cv.put(sleep.COLUMN_TIMESSLEPT, timesSlept);

        long result = db.update(sleep.TABLE_NAME, cv, sleep.COLUMN_USERID + " = ? AND " + sleep.COLUMN_DATE + " = ?", new String[] {String.valueOf(userID), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Sleep timer started!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateSleepStop(int userID, String date, String stopped, String totalSlept) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(sleep.COLUMN_STOPPED, stopped);
        cv.put(sleep.COLUMN_TOTALSLEPT, totalSlept);

        long result = db.update(sleep.TABLE_NAME, cv, sleep.COLUMN_USERID + " = ? AND " + sleep.COLUMN_DATE + " = ?", new String[] {String.valueOf(userID), date});

        if (result == -1) {
            Toast.makeText(context,"Database Error!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Sleep timer stopped!", Toast.LENGTH_SHORT).show();
        }
    }
}
