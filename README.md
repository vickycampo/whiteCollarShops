"# whiteCollarShops" 

## To Create the database:
```
mysql> create database db_whitecollarshops; -- Creates the new database
mysql> create user 'PLfv37Tc*ycBfWB#'@'%' identified by 'jDa%uDW&h2Ed%8nA
'; -- Creates the user
mysql> grant all on db_whitecollarshops.* to 'PLfv37Tc*ycBfWB#'@'%'; -- Gives all privileges to the new user on the newly created database
```
## To import the database
In the folder DB you will find an export. You can import it to your Data Base.

## Fix the time zone error:
```
SET GLOBAL time_zone = '-3:00';
```
