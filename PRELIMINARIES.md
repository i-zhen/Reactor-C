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

Here you can name your repository and give it an optional description. Finally, click "Fork repository" to finish. After forking you should grant the teaching staff read access to your repository. Click on Settings (the gear icon), and then go to "Access management", the window should look similar to the figure below:

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
