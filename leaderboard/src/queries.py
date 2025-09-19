get_highest_scores = "SELECT * FROM scores ORDER BY score DESC LIMIT 5;"
insert_score = "INSERT INTO scores (name, score) VALUES (?, ?);"