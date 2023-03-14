# java-filmorate

### ER диаграмма создана при помощи [dbdiagram.io](https://dbdiagram.io/) ###

![This is a scheme](src/main/resources/imgScheme.png "Схема БД")


### Код ###
```
Table film {
 id int [pk]
 name varchar
 description varchar
 release_date date
 duration bigint
 mpa_id int
}

Ref: film.id < user_like.film_id
Ref: film.mpa_id > mpa.id

Table mpa {
 id int [pk]
 name varchar
 descripton varchar
}

Table genre {
 id int [pk]
 name varchar
}
Ref: genre.id < film_genre.genre_id

Table film_genre {
 film_id int 
 genre_id int
}
Ref: film_genre.film_id > film.id

Table user {
 id int [pk]
 email varchar
 login varchar
 name varchar
 birthday date
}
Ref: user.id < user_like.user_id

Table friends {
 user_id int 
 friend_id int 
 status varchar
}
Ref: friends.user_id > user.id
Ref: friends.friend_id > user.id

Table user_like {
 film_id int 
 user_id int 
}
```