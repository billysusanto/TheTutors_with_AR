package com.thetutors.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thetutors.model.Marque;
import com.thetutors.model.QuestionTest;
import com.thetutors.model.Tutorial;
import com.thetutors.model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "theTutors";

    // Table Names
    private static final String TABLE_QUESTION = "questionTest";
    private static final String TABLE_TUTORCONTENT = "tutorContent";
    private static final String TABLE_MARQUE = "marqueText";
    private static final String TABLE_USER = "user";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table - column names
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_MULTIPLECHOISE = "multiplechoice";
    private static final String KEY_MARQUE = "marque";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    //


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_QUESTION =
            "CREATE TABLE " + TABLE_QUESTION +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_QUESTION + " TEXT," +
            KEY_ANSWER + " TEXT," +
            KEY_MULTIPLECHOISE + " TEXT" +
            KEY_CREATED_AT + " DATETIME"+ ")";

    // Tag table create statement
    private static final String CREATE_TABLE_MARQUE =
            "CREATE TABLE " + TABLE_MARQUE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MARQUE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TUTORCONTENT =
            "CREATE TABLE " + TABLE_TUTORCONTENT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_CONTENT + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER
            + "(" + KEY_ID + "INTEGER PRIMARY KEY, "
            + KEY_USERNAME + " TEXT UNIQUE,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_TUTORCONTENT);
        db.execSQL(CREATE_TABLE_MARQUE);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORCONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARQUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }

    // =====================  QUESTION - START  ============================
    public long createQuestion(QuestionTest qt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, qt.getQuestion());
        values.put(KEY_ANSWER, qt.getAnswer());
        values.put(KEY_MULTIPLECHOISE, qt.getMultipleChoice());
        values.put(KEY_CREATED_AT, DateFormat.getDateTimeInstance()+"");

        // insert row
        long question_id = db.insert(TABLE_QUESTION, null, values);

        return question_id;
    }

    /*
     * get Question Object
     */
    public QuestionTest getQuestion(long question_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE "
                + KEY_ID + " = " + question_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        QuestionTest qt = new QuestionTest();
        qt.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));
        qt.setAnswer((c.getString(c.getColumnIndex(KEY_ANSWER))));
        qt.setMultipleChoice(c.getString(c.getColumnIndex(KEY_MULTIPLECHOISE)));

        return qt;
    }

    public List<QuestionTest> getAllQuestion(){
        List <QuestionTest> qtList = new ArrayList<QuestionTest>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                QuestionTest qt = new QuestionTest();
                qt.setMultipleChoice(c.getString(c.getColumnIndex(KEY_MULTIPLECHOISE)));
                qt.setAnswer(c.getString(c.getColumnIndex(KEY_ANSWER)));
                qt.setQuestion(c.getString(c.getColumnIndex(KEY_QUESTION)));

                qtList.add(qt);
            } while (c.moveToNext());
        }
        return qtList;
    }

    public int updateQuestion(QuestionTest qt){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, qt.getQuestion());
        values.put(KEY_MULTIPLECHOISE, qt.getMultipleChoice());
        values.put(KEY_ANSWER, qt.getAnswer());

        return db.update(TABLE_QUESTION, values, KEY_ID + " = ?",
                new String[] {String.valueOf(qt.getId())});
    }

    public int deleteQuestion(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_QUESTION, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    // =====================  QUESTION - END  =====================


    // =====================  MARQUE - START  =====================
    public long createMarque(Marque mq){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MARQUE, mq.getMarque());

        long marqueId = db.insert(TABLE_MARQUE, null, values);

        return marqueId;
    }


    public void updateMarqueText(String marqueText){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MARQUE, marqueText);

        db.update(TABLE_MARQUE, values, KEY_ID + " = 1", null);
    }

    public String getMarqueText(){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MARQUE;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        String marqueText = c.getString(c.getColumnIndex(KEY_MARQUE));

        return marqueText;
    }

    public int deleteMarque(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_MARQUE, KEY_ID + " = 1", null);
    }

    //=====================  MARQUE - END  =====================

    //=====================  TUTORIAL - START  =====================
    public long createTutorial(Tutorial tutorial){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, tutorial.getTitle());
        values.put(KEY_CONTENT, tutorial.getContent());

        long tutorialId = db.insert(TABLE_TUTORCONTENT, null, values);

        return tutorialId;
    }

    public List <Tutorial> getAllTutorial(){
        SQLiteDatabase db = this.getReadableDatabase();

        List <Tutorial> tutorialList = new ArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_TUTORCONTENT;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                Tutorial tc = new Tutorial();
                tc.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                tc.setContent(c.getString(c.getColumnIndex(KEY_CONTENT)));

                tutorialList.add(tc);
            } while (c.moveToNext());
        }

        return tutorialList;
    }

    public Tutorial getTutorial(int tutorial_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+TABLE_TUTORCONTENT + " WHERE " + KEY_ID + " = " + tutorial_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Tutorial tc = new Tutorial();
        tc.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        tc.setContent((c.getString(c.getColumnIndex(KEY_CONTENT))));

        return tc;
    }

    public void updateTutorial(int id, Tutorial tutorial){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, tutorial.getTitle());
        values.put(KEY_CONTENT, tutorial.getContent());

        db.update(TABLE_TUTORCONTENT, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteTutorial(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_TUTORCONTENT, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    //=====================  TUTORIAL - END  =====================

    //=====================  USER - START  =======================

    public long createUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());

        try {
            long userId = db.insert(TABLE_USER, null, values);
            Log.e("userId", userId + "");
            return userId;
        }
        catch(SQLiteConstraintException e){
            e.printStackTrace();
        }

        return -1;
    }

    public User getUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        User user;

        String selectQuery = "SELECT * FROM "+TABLE_USER + " WHERE " + KEY_USERNAME + " = '" + username + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            try {
                user = new User();
                user.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                user.setPassword((c.getString(c.getColumnIndex(KEY_PASSWORD))));
                return user;
            }
            catch(CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //=====================  USER - END ==========================
    public void CloseDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}