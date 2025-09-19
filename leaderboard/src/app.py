import sqlite3
from typing import List, Dict, Any
import uvicorn
from fastapi import FastAPI, HTTPException

from data_model import Score, ScoresListResponse
from conf import DB_PATH
from queries import get_highest_scores, insert_score

app = FastAPI()


def get_db_conn():
    try:
        return sqlite3.connect(DB_PATH)
    except sqlite3.Error as e:
        raise HTTPException(status_code=500, detail=f"Database connection error: {str(e)}")

@app.get("/scores", response_model=ScoresListResponse)
def read_board() -> ScoresListResponse:
    conn = None
    try:
        conn = get_db_conn()
        cursor = conn.cursor()
        cursor.execute(get_highest_scores)
        top_scores = cursor.fetchall()

        scores_list = []
        for row in top_scores:
            score_dict = {
                "id": row[0],
                "name": row[1],
                "score": row[2]
            }
            scores_list.append(score_dict)

        return ScoresListResponse(
            scores=scores_list,
            total_count=len(top_scores)
        )

    except sqlite3.Error as e:
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")
    finally:
        if conn:
            conn.close()


@app.post("/scores")
def write_to_board(score_payload: Score) -> Dict[str, str]:
    conn = None
    try:
        conn = get_db_conn()
        cursor = conn.cursor()

        cursor.execute(get_highest_scores)
        top_scores = cursor.fetchall()

        if len(top_scores) < 5:
            cursor.execute(insert_score, (score_payload.name, score_payload.score))
            conn.commit()
            return {"message": "Score submitted to the leaderboard!"}
        else:
            lowest_score = min(top_scores, key=lambda x: x[2])[2]
            if score_payload.score > lowest_score:
                cursor.execute(insert_score, (score_payload.name, score_payload.score))
                conn.commit()
                return {"message": "Score submitted to the leaderboard!"}
            else:
                return {"message": "Your score needs to be higher than the current top 5 scores."}

    except sqlite3.Error as e:
        if conn:
            conn.rollback()
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")
    finally:
        if conn:
            conn.close()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)