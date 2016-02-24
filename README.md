# ee461l

Available at: http://trii-chat.herokuapp.com

How to deploy a SparkJava application to Heroku:
https://sparktutorials.github.io/2015/08/24/spark-heroku.html

What you have to do in order to be able to test locally and deploy to Heroku:
- Pull this Git repo
- Import changes from Maven
- Create a heroku account
- Make sure you are added as a collaborator for the trii-chat project
- Install heroku command line tools
- In the terminal, type:
    heroku login
- Add run configurations in IntelliJ
    - for testing locally, create "Application" configuration with main class Main
    - for deploying to heroku, create "Maven" configuration with command line heroku:deploy
    
To see addons:
heroku addons --app trii-chat
heroku config --app trii-chat //this lets you see DATABASE_URL

Heroku Postgres Driver
Import new Maven dependency.
    - On Intellij: Open Library Settings -> Libraries -> + -> From maven -> type org.postgresql -> get 9.4.1208 version
