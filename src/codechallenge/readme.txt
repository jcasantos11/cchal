A. Requirements of the coding Challege
	bad-record_clients_<timestamp>.csv
	bad-record_transactions<timestamp>.csv
	logfile_<timestamp>.txt
B. In your assessment is there something wrong with the requirements?
	There are client entries from transactions that doesnt exist in the clients.csv file. The long file name of the csv file. 
C. If yes, how do you propose the requirements and/or design can be improved?
	Make the filename of the csv files shorter. Or provide column names for the tables. Clients that are not included in the clients.csv file doesnt have any orders for them about what to do which can be included in the bad-record files.
D. Instructions how to compile and use from the command line
	1. on the command line cd to the CodeChallege.jar file location.
	2. type java -jar CodeChallenge.jar.
	3. java program will load and ask for the location of clients.csv file then press enter.
	4. next the java program will ask for the location of transaction.csv file then press enter.
	5. java program will now load table records to the database.
F. Description of different steps in your solution
	1. Learned the regex to split line data from csv file so every column will be accessed.
	2. Setup the database for the project.
	3. Create and Customize the database tables.
	4. Created statements to insert entries to the tables.
	5.  Added a new column(_clientid) for the transaction table.
	6. Save bad records in a csv file for each tables.
	7. Saved a logfile for reports.
	8. Added a filename entry in start up.
	9. Compiled the program into a jar file for checking.
G. DDL Commands
CREATE DATABASE codechallenge

CREATE TABLE clients (
    _clientid int NOT NULL AUTO_INCREMENT,
    _clientname varchar(255) NOT NULL,
    _clientnumber varchar(255),
    _clientaddress varchar(255),
    _clientsince date,
    _clientbranch varchar(255),
    PRIMARY KEY(_clientid)
)
CREATE TABLE transactions (
    _transactionid int NOT NULL AUTO_INCREMENT,
    _clientname varchar(255) NOT NULL,
    _clientid int NOT NULL,
    _paymode varchar(255),
    _itemname varchar(255),
    _netamount double,
    _vatamount double,
    _branch varchar(255),
    _timestamp timestamp,
    PRIMARY KEY (_transactionid)
)
H. REFERENCES
https://rjcodeblog.wordpress.com/2013/09/05/regex-to-split-a-string-on-comma-outside-double-quotes/
https://www.youtube.com/watch?v=KRhv4iPgzHE
https://www.youtube.com/watch?v=HE6ZHSuHcu0
https://www.youtube.com/watch?v=ru2Mqs5AUuo
http://www.baeldung.com/java-write-to-file
https://stackoverflow.com/questions/1097332/convert-a-string-to-double-java
http://chortle.ccsu.edu/java5/notes/chap23/ch23_14.html