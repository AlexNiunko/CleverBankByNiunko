Название проекта -  CleverBank

Данный проект представляет собой  клиент-серверное приложение, реализованное с помощью Jakarta EE(Servlets).

Для запуска необходимо установить:
 - JDK17
 - Apache Tomcat v.10.0.61
 - PostrgreSQL
 - IntelliJ IDEA 2022.2.3
   Возможности клиента:
   -Авторизация
   -Регистрация
   -Открытие банковского счета
   -Закрытие банковского счета
   -Пополнение банковского счета
   -Снятие средств с банковского счета
   -Перевод на другой банковский счет.
   
   Также раз в полминуты выполняется проверка требуется ли начислить проценты на счет 25 чила каждого месяца (Перечень счетов хранится в файле info.yml в директории resources).
   После каждой операции формируется чек и сохраняется в директории webapp\check. Операции снятия , пополнения и перевода средств со счета реализованы с соблюдением принципов ACID.
   Для каждой сущности реализованы операции CRUD. В рамках данного проекта курс конверсии везде 1.0 .
   ![image](https://github.com/AlexNiunko/CleverBankByNiunko/assets/63747979/9403a00c-a083-451e-9eb2-25350b6eb567)

   
   

