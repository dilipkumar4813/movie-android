# Popular Movies

Android application that will show a list of movies - either popular, top rated or favourites and provide information on each individual movie with plot, image, rating and more.

## Features

* Load popular, top rated and favourites movies
* Share text
* View movie details
* Landscape view
* Infinite scroll
* Trailers for movies
* Reviews for the movies

## Movies list

The screen will load the popular movies when the screen is loaded, the user can change the sort by using the menu option in the action bar and can also share the app using the share icon in the action bar

![Movies list](https://github.com/dilipkumar4813/movie-android/blob/master/screenshots/Screenshot_2017-04-22-21-25-00.png)

## Movies detail

The screen will show the details of the movie that has been selected and will show the following info

* Banner
* Poster
* Title
* Release date
* Average rating
* Language
* Plot

![Movie details](https://github.com/dilipkumar4813/movie-android/blob/master/screenshots/Screenshot_2017-04-22-21-24-32.png)

## How to use the source code

Download or clone the repository on the your local machine, register in [The movie database](https://www.themoviedb.org/?language=en). Create an API using the instructions provided in the website, once you have received the API key copy and paste it within the gradle.properties. Build and run the application.

The project uses jack options and java 8, If you face issues while configuring the project use the below instructions
1. Commenting the jackOptions enable true block, and the compileOptions block, where I had it compatible with 1.8
2. Sync, Clean and rebuild the project
3. Uncomment jackOptions and compileOptions in Gradle
4. Sync, Clean and rebuild the project project

## Libraries

* [Picasso](http://square.github.io/picasso/)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Rxjava 2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0)
* [Retrofit 2](http://square.github.io/retrofit/)
* [Gson](http://square.github.io/retrofit/)
* [Retrofit2 Rxjava2 adapter](https://github.com/JakeWharton/retrofit2-rxjava2-adapter)
* [Logging interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
