# Deadlines #

1. [Part 1 (parser)](desc/PART1/),  Thursday 15 October 2015  at 4pm, weight = 10%
2. [Part 2 (ast builder)](desc/PART2),  Thursday 29 October 2015  at 4pm, weight = 20%
3. [Part 3 (semantic analyser)](desc/PART3),  Thursday 12 November 2015 at 4pm, weight = 20%
4. [Part 4 (code generator)](desc/PART4), Thursday 3  December 2015 at 4pm, weight = 50%

# Scoreboard #

We automatically run a series of hidden test programs using your compiler about once a day. You can keep track of your progress and see how many tests pass/fail using the scoreboard by following this link: (http://htmlpreview.github.io/?https://bitbucket.org/cdubach/ct-15-16/raw/master/scoreboard/scoreboard.html).

# Marking #

The marking will be done using an automated test suite on a dice machine using Java 7 (1.7 runtime).
Please note that you are not allowed to modify the `Main.java` file which is the main entry point to the compiler.
A checksum on the file will be performed to ensure the file has not be tempered with.
Also make sure that the build script provided remains unchanged so that your project can be built on dice.
Furthermore, you may not use any external libraries.

# Setup #

## Bitbucket ##
We will rely on bitbucket and it is mandatory to use it for this coursework.
Bitbucket is an online repository that can be used with the git control revision system.

Your first task should be to setup a bitbucket account using your university email address.
You should then send to [daniel.hillerstrom@ed.ac.uk](daniel.hillerstrom@ed.ac.uk) your bitbucket account id so that we can run the automated test suite on your repository.
Details on how to fork the CT-15-16 repository are given below.
Important: do not share your code and repository with anyone and keep your source code secret.
If we identify that two students have identical portion of code, both will be considered to have cheated.

## Development environment (editor)
You can choose to use a development environment for your project. DICE machines have the following integrated development environments (IDE) for Java installed:

* Community edition of [IntelliJ](https://www.jetbrains.com/idea/).
* [Eclipse](https://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr) for Java.

Alternatively, you can use Emacs, vim, or your favourite text editor. Choose whichever you are confident with.

## Obtaining your own copy of the CT-15-16 repository 
We are going to be using the Git revision control system during the course. Git is installed on DICE machines. If you use your own machine then make sure to install Git.

You will need to have your own copy of the CT-15-16 repository. In order to fork this repository hover the cursor over the "three dots"-icon to the left, and then click "Fork" as shown in the figure below:

![Forking the CT-15-16 repository](/figures/howtofork.png "Forking this repository.")

Thereafter you will see a form similar to the below figure:

![Forking the CT-15-16 repository](/figures/forking.png "Forking this repository.")

Here you can name your repository and give it an optional description. **Remember** to tick "This is a private repository". Finally, click "Fork repository" to finish. After forking you should grant the teaching staff read access to your repository. Click on Settings (the gear icon), and then go to "Access management", the window should look similar to the figure below:

![Granting the teaching staff read access](/figures/repopermissions.png "Granting the teaching staff read access.")

You should grant the following users read access:

* Christophe Dubach (username: cdubach)
* Daniel Hillerström (username: dhil)

Next, you will have to clone the forked repository to your local machine. In order to clone the repository you should launch a terminal and type:
```
$ git clone https://YOUR-USERNAME@bitbucket.org/YOUR-USERNAME/YOUR-REPOSITORY-NAME
```
where `YOUR-USERNAME` must be *your* Bitbucket account name, and `YOUR-REPOSITORY-NAME` must be the name you chose for your fork of the CT-15-16 repository.

## Building the CT-15-16 project
In order to build the project you must have Ant installed. On DICE machines Ant is already installed.
Your local copy of the CT-15-16 repository contains an Ant build file (`build.xml`). If you are using an IDE, then you can import the build file. Otherwise, you can build the project from the commandline by typing:
```
$ ant build
```
This command outputs your compiler in a directory called `bin` within the project structure. Thereafter, you can run your compiler from the commandline by typing:
```
$ java -cp bin Main
```
The parameter `cp` instructs the Java Runtime to include the local directory `bin` when it looks for class files.

You can clean the `bin` directory by typing:
```
$ ant clean
```
This command effectively deletes the `bin` directory.

## Working with git and pushing your changes

Since we are using an automated marking mechnism (based on how many progams can run successfully through your compiler), it is important to understand how git works. If you want to benefit from the nightly automatic marking feedback, please ensure that you push all your changes daily onto your bitbucket centralised repository.

We suggest you follow the excelent tutorial from atlassian on how to use git. In particular you will need to understand the following basic meachnisms:

* [add and commit](https://www.atlassian.com/git/tutorials/saving-changes)
* [push](https://www.atlassian.com/git/tutorials/syncing/git-push)