#### Приложение для просмотра информации о кино, информация загружается с API сайта [TheMovieDB](https://www.themoviedb.org/documentation/api)  
Приложение в разработке, но его можно запустить и опробовать. Создается в первую очередь в учебных целях.  
Минимальная версия API Android: 19

#### Используемые инструменты и библиотеки  
Язык: Java  
Работа с сетью, JSON и изображениями: RxJava, RxAndroid, Retrofit, Gson, Picasso  
Android Architecture Components: Room, ViewModel, LiveData  

#### Описание функционала  
- На главном экране отображается список популярных фильмов в виде постеров, реализована бесконечная прокрутка и подгрузка данных.  
- Используется RecyclerView с DiffUtils для оптимизации.  
- Загруженные фильмы сохраняются на устройстве (Room) и информацию о них можно просмотреть в оффлайн режиме.  
- Picasso настроен таким образом, что изображения также берутся из кеша, даже если приложение перезапущено и интернет включен.  
- Результаты от API (названия фильмов на постерах и т.д.) приходят на языке, установленном в ОС.  
- В тулбаре реализован живой поиск с предложениями, поиск начинается через 0.5с после того, как завершится ввод поисковой строки, 
чтобы не отправлять API слишком много запросов.  
- Все меню на текущий момент это заглушки, по клику на фильм показывается его название, по клику на результат поиска ничего не происходит.   
- В будущем можно будет переключить способ сортировки, сменить фильмы на сериалы, добавлять в избранное и не только :)    

#### Важно!  
В исходных файлах проекта нет API ключа, его хранение реализовано способом, подробно описанным в 
[статье (eng)](https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906)  
После клонирования проекта необходимо положить ключ в файл gradle.properties, в формате:  
```TMDB_API_KEY="89c...ab1"```  
Этот файл находится в папке: ```C:\Users\Username\.gradle\``` , если файла там не будет, просто создайте его.  
Получить ключ вы можете бесплатно, после регистрации на [сайте](https://www.themoviedb.org/documentation/api).  

![Скриншот](https://i.imgur.com/yJUhb20.png "Скриншот")