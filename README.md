# java-filmorate
## ER DIAGRAM

![Filmorate-final](https://user-images.githubusercontent.com/97036914/175804108-814d98bc-e5c5-40b2-865e-85765d9cb224.png)


## SQL requests examples
**Get all films**  

    SELECT * FROM filmStorage
    
**Get all users**
    
    SELECT * FROM userStorage

**Get 10 most popular films**  

    SELECT fs.name,  
          COUNT(fl.user_id) AS likes
    FROM film_likes AS fl
    INNER JOIN filmStorage AS fs
          ON fs.film_id = fl.film_id
    GROUP BY  fl.name
    ORDER BY  likes DESC 
    LIMIT 10;

**Get common friends**

    SELECT us.name
    FROM friends AS fr
    WHERE u.user_name = 'user1'
		      OR u.user_name = 'user2'
		      AND fs.friendship_status = 'confirmed'
    INNER JOIN userStorage AS us
          ON fr.friend_req_id = us.user_id
    INNER JOIN friendship_status AS fs
          ON fr.friendship_status_id = fs.friendship_status_id
    GROUP BY  fr.friend_resp_id
    HAVING COUNT(fr.friend_resp_id)>1;
    
