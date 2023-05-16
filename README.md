# java-filmorate
Template repository for Filmorate project.
![Иллюстрация к проекту](https://github.com/Vasily-Maximov/java-filmorate/blob/main/db_diagram.png)

Пример запросов:
-- вывести все фильмы с рейтингами по убыванию
select f."name" , count(l.user_id) as count_likes  from films as f join likes l ON f.id = l.film_id group by f."name" order by count_likes  desc; 

-- вывести самый лучший фильм по рейтингу
select f.*, m."name" as mpa from films f   join mpa m ON f.mpa_id  = m.id where f.id in (select l.film_id from likes l group by l.film_id order by count(l.film_id) desc limit 1);
