# TMDApp

Приложение для поиска фильмов с [TheMovieDB](https://www.themoviedb.org/).

 <img src="../screenshots/screenshots/Screenshot_Search_Movies.png" width="300"> <img src="../screenshots/screenshots/Screenshot_Movie_Details.png" width="300">

## Структура проекта

В проекте используется многомодульная архитектура:

- **app:** содержит стартовую активити приложения, application класс, основной манифест;

- **core:** содержит общие вспомогательные классы, абстрактные классы, интерфейсы, экстеншены;

- **database:** содержит описание базы данных, сущностей, дао классов, миграций;

- **test:** содержит вспомогательные классы для тестирования;

- **features:** содержит подмодули отдельных фич в проекте;

  - **features/authentication:** модуль логина и авторизации;

  - **features/main:** основной модуль с экранами поиска фильмов, избранным и профилем.
  
  

Проект придерживается модели ведения рабочего процесса Gitflow.

Архитектура в стиле Clean Architecture:

feature модули разбиты на presentation, domain и data слои, каждый из которых разбит на свои подслои.

Большинство модулей покрыты unit тестами и интеграционными тестами.

## Безопасность:

- Следование рекомендациям OWASP Mobile Top 10;
- Проверка на root права;
- https;
- ssl-pinning;
- авторизация по пин-коду;
- авторизация по отпечатку пальца;
- шифрование пользовательских данных;
- обфускация кода (proguard);
- автоматическое отключение логов для релизной сборки.

## Архитектура: 

Multi-module architecture, Clean Architecture, Презентационный паттерн - MVVM.

## Библиотеки:

Material Components, Navigation Components, Dagger 2, RxJava 2, RxBinding, Moshi, Retrofit, Timber, Junit5, Spek, AssertJ, MockitoKotlin,
Picasso, Groupie, Tink, Rootbeer, Room
