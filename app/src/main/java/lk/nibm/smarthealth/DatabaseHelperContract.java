package lk.nibm.smarthealth;

public class DatabaseHelperContract {
    private DatabaseHelperContract() {}

    public static class users {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_LASTNAME = "lastname";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_WATERINFO = "waterinfo";
        public static final String COLUMN_SLEEPSTARTED = "sleepstarted";
        public static final String COLUMN_SLEEPPAUSED = "sleeppaused";
    }

    public static class bmi {
        public static final String TABLE_NAME = "bmi";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USERID = "userid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_BMI = "bmi";
    }

    public static class notes {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USERID = "userid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
    }

    public static class water {
        public static final String TABLE_NAME = "water";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USERID = "userid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CAPACITY = "capacity";
        public static final String COLUMN_CUP = "cup";
        public static final String COLUMN_CURRENT = "current";
        public static final String COLUMN_PERCENTAGE = "percentage";
    }

    public static class sleep {
        public static final String TABLE_NAME = "sleep";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USERID = "userid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STARTED = "started";
        public static final String COLUMN_STOPPED = "stopped";
        public static final String COLUMN_TOTALSLEPT = "totalslept";
        public static final String COLUMN_TOTALPAUSED = "totalpaused";
        public static final String COLUMN_TIMESSLEPT = "timesslept";
    }
}
