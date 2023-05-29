DROP TABLE IF EXISTS mpa, genres, films, film_genres, users, friends, likes CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
	id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar(50) NOT NULL,
	CONSTRAINT mpa_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres (
	id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar(50) NOT NULL,
	CONSTRAINT genres_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films (
	id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar(50) NOT NULL,
	description varchar(200) NOT NULL,
	release_date date NOT NULL,
	duration integer NOT NULL,
	mpa_id integer NOT NULL,
	CONSTRAINT films_pk PRIMARY KEY (id),
	CONSTRAINT films_fk FOREIGN KEY (mpa_id) REFERENCES mpa(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genres (
	film_id integer NOT NULL,
	genre_id integer NOT NULL,
	CONSTRAINT film_genres_fk_film FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT film_genres_fk_genres FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
	id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	email varchar(50) NOT NULL,
	login varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
	birthday date NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS friends (
	user_one_id integer NOT NULL,
	user_two_id integer NOT NULL,
	status boolean NOT NULL,
	CONSTRAINT friends_user_one_fk FOREIGN KEY (user_one_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT friends_user_two_fk FOREIGN KEY (user_two_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
	film_id integer NOT NULL,
	user_id integer NOT NULL,
	CONSTRAINT likes_film_fk FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT likes_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);