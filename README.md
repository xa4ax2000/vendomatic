<h1>vendomatic</h1>
<h4>Service which will support a beverage vending machine</h4>
<section>
    <h6>Requirements to set-up locally</h6>
    <ul>
        <li> <a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">Java 8 (minimum) JDK</a> </li>
        <li> <a href="https://maven.apache.org/download.cgi"> Maven </a> </li>
        <li> <a href="https://www.jetbrains.com/idea/download/#section=windows">Your preferred IDE (optional step)</a></li>
    </ul>
    <h6>Setting up Java</h6>
    <ol>
        <li>
            Install Java from the link above; if it is an installer, follow through the installation instructions to install the JDK to your box.
        </li>
        <li>
            Make sure java bin directory is listed in your environmental variables (Path) and JAVA_HOME is correctly set.
        </li>
        <li>
            If installed correctly, you should be able to check the version on a terminal via the following command:
            <code>
                java --version
            </code>
        </li>
    </ol>
    <h6>Setting up Maven</h6>
    <ol>
        <li>
            Install Maven from the link above. Install the bin version not the src!
        </li>
        <li>
            Follow the tutorial in this link for assistance: <a href="https://crunchify.com/how-to-setupinstall-maven-classpath-variable-on-windows-7/">https://crunchify.com/how-to-setupinstall-maven-classpath-variable-on-windows-7/</a>
        </li>
        <li>
            If installed correctly, you should be able to check the version on a terminal via the following command:
            <code>
                mvn -version
            </code>
        </li>
    </ol>
    <h6>Setting up your IDE</h6>
    <p>
        This section is dependent on the IDE (Please contact andrew.m.hyun@gmail.com if you wish for me to update this section)
        <br/>
        <b>Please Note</b>: This is <u>NOT</u> required for running the application.
    </p>
</section>
<section>
    <h6>To run the service</h6>
    <ol>
        <li>
            Clone project from github to a directory
        </li>
        <li>
            Open terminal
        </li>
        <li>
            Change directory to where you cloned the project 
        </li>
        <li>
            run the run.sh bash script 
            <br>
            If you're wary of the file you can just run the following commands:
            <br>
            <code>
                mvn clean package
            </code>
            <br>
            <code>
                cd target
            </code>
            <br>    
            <code>
                java -jar vendomatic-0.0.1-SNAPSHOT.jar
            </code>
        </li>
        <li>
            curl the HTTP requests you wish to test
            <br>
            <br>
            Sample request message:
            <br>
            <code>
                curl -X PUT "http://localhost:8080/" -H "Content-Type: application/json" -d "{\"coin\":1}" -verbose
            </code>
        </li>
    </ol>
</section>
<section>
    <h6>Still having issues?</h6>
    <p>
        Please contact andrew.m.hyun@gmail.com for further assistance (and this README will be updated accordingly)
    </p>
</section>