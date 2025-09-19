import sqlite3
import os
from conf import DB_PATH

os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)

conn = sqlite3.connect(DB_PATH)

cursor = conn.cursor()

create_table_query = """
    CREATE TABLE IF NOT EXISTS scores (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        score INTEGER NOT NULL
    );
    """

cursor.execute(create_table_query)

conn.commit()
conn.close()

print(f"Database and table 'scores' created (or already exists) at {DB_PATH}")