## Solution of Chapter 4

### A Simple Demo
[![Solution Demo](images/screenshot.png)](https://www.youtube.com/watch?v=HNdGkdN7U0c&feature=youtu.be)

### Run at Terminal
```cmd
:: under java-tutorial root directory
mvn clean package
cd chapter4
:: make sure JDK11 is before JDK8 if they were all in path
set PATH=C:\Program Files\Java\jdk-11.0.2\bin;%PATH%;
:: for linux, replace ';' with ':'
java -cp "target/deps/*;target/classes/" Game
```

### Attention, attention, please
* From Chapter 4 on, solutions will be developed and tested under JDK11
* Please set Working Directory as `$MODULE_DIR$` because this is a multi-module project
* You can add VM options `-splash:images/champions.jpg` to enable splash feature
* You can add argument `Start` to skip home team, visiting team and stadium selection and apply
default settings directly
* You may be disappointed that No AI here - something you never expect to learn from this course
* Players, referees and audience employ simply random selection strategy to determine their behavior
* No fancy GUI stuff here, just use simple Graphics API to draw or fill rectangle, arc and etc

### Contribution & Bugs
You just don't. No pull request, no bug report. Just let it be. Thank you man.😅

### Last Words
Coding, and  
Keep coding, make it work  
And keep coding, make it better  
And keep coding, make it easier to maintain  
And keep coding, to understand or reject  
Those buzz words of OOP  
And keep coding