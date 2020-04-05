#Конвертер
####Реализация
В рамках данного проекта мною были также реализованы лексический и синтаксический анализаторы, благодаря чему скорость анализа увеличилась из-за того, что не использовались регулярные выражения. По большей части это было сделано ради интереса.

Парсинг выражений осуществляется Алгоритмом сортировочной станции с построением синтаксического дерева, что в делает дальнейшие преобразования оптимальными.

####Упрощение
Я сделал несколько случаев упрощения итогового выражения. Упростил узлы такого типа

#####Первое:

&nbsp;&nbsp;&nbsp;&nbsp;|operator|

|N1|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|N2| 
 
в

|N1 operator N2|

Т.е. 3 узла превратились в один

#####Второе:
(N1*X)*N2  => ((N1\*N2)*X)

4 узла превращаются в 3 (в всех вариациях для *)

Аналогично можно сделать с другими арифметическими операциями, но там необходимо тщательное тестирование, чтобы нигде не ошибиться, поэтому в рамках дедлайна я не стал этого делать

Упрощение логических выражений так же не сделал, там всё гораздо интереснее. Пришлось бы создавать объект для представления смыла логического выражение и методы, такие, как пересечение, включение и т.д.

#### Тестирование
Подготовил модульные тесты, также написал программу, тестирующую выражения "на лету" с помощью исполнения кода из JS, но данная возможно оказалась очень медленной.
