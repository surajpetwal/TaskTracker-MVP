import sqlite3
import os
import json
from datetime import datetime

class TaskDatabase:
    def __init__(self, db_name='tasks.db'):
        self.db_path = db_name
        self.conn = None
        self.cursor = None
        self.init_database()
    
    def init_database(self):
        """Initialize SQLite database with task table"""
        try:
            self.conn = sqlite3.connect(self.db_path)
            self.cursor = self.conn.cursor()
            
            self.cursor.execute('''
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    points INTEGER DEFAULT 0,
                    status TEXT DEFAULT 'pending',
                    created_date TEXT,
                    due_date TEXT,
                    completed_date TEXT,
                    category TEXT,
                    priority INTEGER DEFAULT 1
                )
            ''')
            
            self.cursor.execute('''
                CREATE TABLE IF NOT EXISTS daily_quota (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT UNIQUE,
                    target_points INTEGER DEFAULT 100,
                    completed_points INTEGER DEFAULT 0
                )
            ''')
            
            self.conn.commit()
            print(f"[DB] Database initialized at {self.db_path}")
        except Exception as e:
            print(f"[ERROR] Database initialization failed: {e}")
    
    def save_task(self, title, description="", points=10, due_date=None, category="general", priority=1):
        """Save a new task to database"""
        try:
            created_date = datetime.now().isoformat()
            self.cursor.execute('''
                INSERT INTO tasks (title, description, points, status, created_date, due_date, category, priority)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ''', (title, description, points, 'pending', created_date, due_date, category, priority))
            self.conn.commit()
            print(f"[DB] Task saved: {title}")
            return self.cursor.lastrowid
        except Exception as e:
            print(f"[ERROR] Failed to save task: {e}")
            return None
    
    def load_tasks(self, status="pending"):
        """Load tasks from database"""
        try:
            self.cursor.execute('SELECT * FROM tasks WHERE status = ?', (status,))
            tasks = self.cursor.fetchall()
            print(f"[DB] Loaded {len(tasks)} tasks with status: {status}")
            return tasks
        except Exception as e:
            print(f"[ERROR] Failed to load tasks: {e}")
            return []
    
    def update_task(self, task_id, **kwargs):
        """Update a task"""
        try:
            allowed_fields = ['title', 'description', 'points', 'status', 'due_date', 'category', 'priority']
            updates = {k: v for k, v in kwargs.items() if k in allowed_fields}
            
            if not updates:
                print("[WARNING] No valid fields to update")
                return False
            
            set_clause = ", ".join([f"{k} = ?" for k in updates.keys()])
            values = list(updates.values()) + [task_id]
            
            self.cursor.execute(f'UPDATE tasks SET {set_clause} WHERE id = ?', values)
            self.conn.commit()
            print(f"[DB] Task {task_id} updated: {updates}")
            return True
        except Exception as e:
            print(f"[ERROR] Failed to update task: {e}")
            return False
    
    def delete_task(self, task_id):
        """Delete a task"""
        try:
            self.cursor.execute('DELETE FROM tasks WHERE id = ?', (task_id,))
            self.conn.commit()
            print(f"[DB] Task {task_id} deleted")
            return True
        except Exception as e:
            print(f"[ERROR] Failed to delete task: {e}")
            return False
    
    def close(self):
        """Close database connection"""
        if self.conn:
            self.conn.close()
            print("[DB] Database connection closed")

if __name__ == "__main__":
    db = TaskDatabase()
    db.save_task("Complete project", "Finish MVP", points=50, category="work")
    db.save_task("Exercise", "30 min run", points=20, category="health")
    tasks = db.load_tasks()
    db.close()