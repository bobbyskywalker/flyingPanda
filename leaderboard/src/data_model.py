from pydantic import BaseModel
from typing import List

class Score(BaseModel):
    name: str
    score: int

class ScoreResponse(BaseModel):
    id: int
    name: str
    score: int

class ScoresListResponse(BaseModel):
    scores: List[ScoreResponse]
    total_count: int