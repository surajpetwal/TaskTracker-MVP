package com.surajpetwal.tasktracker.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tasktracker.model.Task

class TaskDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskTracker.db"
        private const val TABLE_TASKS = "tasks"
        
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_POINTS = "points"
        private const val COLUMN_IS_COMPLETED = "is_completed"
        private const val COLUMN_CREATED_DATE = "created_date"
        private const val COLUMN_DUE_DATE = "due_date"
        private const val COLUMN_DAILY_QUOTA = "daily_quota"
        private const val COLUMN_IS_MISSED = "is_missed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_POINTS INTEGER DEFAULT 1,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_CREATED_DATE TEXT NOT NULL,
                $COLUMN_DUE_DATE TEXT,
                $COLUMN_DAILY_QUOTA INTEGER DEFAULT 10,
                $COLUMN_IS_MISSED INTEGER DEFAULT 0
            )
        """.trimIndent()
        
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun insertTask(task: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_POINTS, task.points)
            put(COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(COLUMN_CREATED_DATE, task.createdDate)
            put(COLUMN_DUE_DATE, task.dueDate)
            put(COLUMN_DAILY_QUOTA, task.dailyQuota)
            put(COLUMN_IS_MISSED, if (task.isMissed) 1 else 0)
        }
        
        return db.insert(TABLE_TASKS, null, contentValues)
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor: Cursor?
        
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS ORDER BY $COLUMN_CREATED_DATE DESC", null)
        } catch (e: Exception) {
            db.execSQL("SELECT * FROM $TABLE_TASKS ORDER BY $COLUMN_CREATED_DATE DESC")
            return emptyList()
        }
        
        var id: Int
        var title: String
        var description: String?
        var points: Int
        var isCompleted: Boolean
        var createdDate: String
        var dueDate: String?
        var dailyQuota: Int
        var isMissed: Boolean
        
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS))
                isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
                createdDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))
                dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))
                dailyQuota = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_QUOTA))
                isMissed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_MISSED)) == 1
                
                tasks.add(
                    Task(
                        id = id,
                        title = title,
                        description = description,
                        points = points,
                        isCompleted = isCompleted,
                        createdDate = createdDate,
                        dueDate = dueDate,
                        dailyQuota = dailyQuota,
                        isMissed = isMissed
                    )
                )
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        return tasks
    }

    fun updateTask(task: Task): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_POINTS, task.points)
            put(COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(COLUMN_DUE_DATE, task.dueDate)
            put(COLUMN_DAILY_QUOTA, task.dailyQuota)
            put(COLUMN_IS_MISSED, if (task.isMissed) 1 else 0)
        }
        
        return db.update(TABLE_TASKS, contentValues, "$COLUMN_ID = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TASKS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getTasksByDate(date: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor: Cursor?
        
        try {
            cursor = db.rawQuery(
                "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_CREATED_DATE LIKE ? ORDER BY $COLUMN_CREATED_DATE DESC",
                arrayOf("$date%")
            )
        } catch (e: Exception) {
            return emptyList()
        }
        
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS))
                val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
                val createdDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))
                val dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))
                val dailyQuota = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_QUOTA))
                val isMissed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_MISSED)) == 1
                
                tasks.add(
                    Task(
                        id = id,
                        title = title,
                        description = description,
                        points = points,
                        isCompleted = isCompleted,
                        createdDate = createdDate,
                        dueDate = dueDate,
                        dailyQuota = dailyQuota,
                        isMissed = isMissed
                    )
                )
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        return tasks
    }

    fun getMissedTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor: Cursor?
        
        try {
            cursor = db.rawQuery(
                "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_IS_MISSED = 1 ORDER BY $COLUMN_CREATED_DATE DESC",
                null
            )
        } catch (e: Exception) {
            return emptyList()
        }
        
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS))
                val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
                val createdDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))
                val dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))
                val dailyQuota = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_QUOTA))
                val isMissed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_MISSED)) == 1
                
                tasks.add(
                    Task(
                        id = id,
                        title = title,
                        description = description,
                        points = points,
                        isCompleted = isCompleted,
                        createdDate = createdDate,
                        dueDate = dueDate,
                        dailyQuota = dailyQuota,
                        isMissed = isMissed
                    )
                )
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        return tasks
    }
}
