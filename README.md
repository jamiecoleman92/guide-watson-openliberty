# Open Liberty Watson Workshop

### What you'll learn

You will learn how to build and deploy an instance of the Watson Tone Analyser service on IBM Cloud, and access this within your application hosted on IBM Open Liberty. It will be built with Gradle and served up on a front-end web
application connected to a Java EE backend, using servlets and CDI.

You will submit sentence(s) to be analysed by Watson into the front-end of the application, which the Java backend will make a GET request to your instance of the Watson Tone Analyser Service.
This request returns a JSON representation of the tones identified within the input, and these are formatted and displayed on the front-end web page provided in the tutorial.



This contains information about the tones identified within your input, such as ‘Analytical’, ‘Anger’, ‘Joy’. These are rated from zero to one, and only the tones with scores greater than 0.5 are returned.
A score greater than 0.75 indicates a high likelihood that the tone is perceived in the content.

### Overview of Watson Tone Analyser

The IBM Watson™ Tone Analyser service uses linguistic analysis to detect emotional and language tones in written text. You can use the service to understand how your written communications are perceived and then to improve the tone of your communications. Businesses can use the service to learn the tone of their customers' communications and to respond to each customer appropriately, or to understand and improve their customer conversations.

Follow [this link](https://tone-analyzer-demo.ng.bluemix.net/?cm_mc_uid=17025728856915591373241&cm_mc_sid_50200000=75962731560158357593&cm_mc_sid_52640000=35883971560158357617) for example use cases and more information about the analyser.

## Prerequisites

### Configuring the Watson Tone Analyser Service Instance

Before you can start this tutorial, you need to create an instance of the **Watson Tone Analyser** service on your **IBM Cloud** account.

#### To create an instance of the Tone Analyser service:
1.	Go to the [Tone Analyser](https://cloud.ibm.com/catalog/services/tone-analyzer) page in the IBM Cloud Catalogue.
2.	Sign up for a free IBM Cloud account or login.
3.	Assign the service name to **Tone Analyser Open Liberty**.
4.	Set the region to **London**.
5.	Click Create.

#### Copy the credentials to authenticate to your Tone Analyser service instance
1. Within your [IBM Cloud dashboard](https://cloud.ibm.com/resources), underneath services, select **Tone Analyser Open Liberty**.
2. Select the Manage tab on the left of the screen if it is not already selected.
3. Copy the API Key and URL values for later use.



## Step 1 - Getting Started

The fastest way to work through this guide is to clone the Git repository and use the projects that are provided inside:

```
git clone https://github.ibm.com/Engagement-Demos/Liberty-Watson-Cloud-Workshop
cd Liberty-Watson-Cloud-Workshop
```

The start directory contains the starting project that you will build upon.

The finish directory contains the finished project that you will build.

#### Try what you’ll build

The `/finish` directory in the root of this guide contains the finished application. Give it a try before you proceed.

To try out the application, you must first go to the finish directory and edit the `Analyser` Java file, located at
`src/main/java/io/openliberty/guides/watson/toneanalyser/`. Replace the value of the `apiKey` and `endpoint` fields to
be equal to the values you copied from the credentials from the **IBM Cloud dashboard**.

```Java
  private String apiKey = /*Insert your Watson Tone Service API key here!*/
  private String endpoint = /*Insert your Watson Tone Service URL here!*/;
```

Save this file, then run the following commands to give yourself permissions to run Gradle, then run the Gradle commands to **build** the application inside **Open Liberty**, and then **launch** the server:

```
cd finish
chmod a+x gradlew
./gradlew clean build
./gradlew libertyRun
```

Navigate to the front-end in a browser at the http://localhost:9080/ToneAnalyser/ URL.

After you are done checking out the application, stop the Open Liberty server:

```
./gradlew libertyStop
```

Navigate to the start directory to continue with the guide:

```
cd ../start
```

## Step 2 - Acquiring Watson Tone Analyser Dependencies with Gradle

Before you can start programming with the Watson Tone Analyser service, you need to acquire the Watson Tone Analyser libraries for use in our application using Gradle. If you are unfamiliar with Gradle, please refer to [this guide](https://openliberty.io/guides/gradle-intro.html).

You will need to update the **dependencies** block inside the `build.gradle` file, located in the `/start` directory. You will see two **dependencies** blocks.
Place the following line inside the **dependencies** block which is **_not_** inside the **buildscript** block:

```
compile group: 'com.ibm.watson', name: 'tone-analyzer', version: '7.0.0'
```

This tells Gradle which dependencies need to be retrieved when compiling the application, and these dependencies are retrieved from online repositories, denoted in the
repositories block.

You may have noticed the other dependencies which are already present within the **build.gradle** file. These are required later on within our application. Please do
not remove any of these.

## Step 3 - Creating the Analyser Class

Now, you can begin writing code to call your Watson Tone Analyser service instance which we created in step 2. We will create one class using CDI, and within this class, we will have four fields, an authentication method (to authenticate with our Watson Tone Analyser instance!), and a method to return a tone analysis of an input string.

You will have a lot of import complains on objects. Simply import these as you go.

Create a class called `Analyser`, in the directory `/src/main/java/io/openliberty/guides/watson/toneanalyser/`.

```Java
/**
 * Class to handle logic for requesting the Watson Tone Analyser service. The
 * API key is currently stored in plain text. This is bad practice, but
 * sufficient for the tutorial.
 *
 * @author Scott C Curtis
 */
@ApplicationScoped
public class Analyser {

	// fields to authenticate with the service
	private String apiKey = /*Insert your Watson Tone Service API key here!*/
	private String version = "2017-09-21";
	private String endpoint = /*Insert your Watson Tone Service URL here!*/;

	// field for processing the input
	private ToneAnalyzer toneAnalyser;

	/**
	 * Authenticates with IAM, and creates the tone analyser service. The apiKey and
	 * endpoint can be found on the user's cloud dashboard. The version is the
	 * latest version of the tone analyser.
	 */
	@PostConstruct
	public void authenticate() {
    //To be completed
	}

	/**
	 * Makes a request to the ToneAnalyser service, given an input string, and
	 * returns the tone analysis of the input.
	 *
	 * @param input - the string to be analysed.
	 * @return a ToneAnalysis object, containing the input string, and the
	 *         associated tones for the input.
	 */
	public ToneAnalysis analyseInput(String input) {
      //To be completed
	}

}
```

### Code Walkthrough

#### Class Annotation
`@ApplicationScoped` ensures that only one instance of our `Analyser` class exists at runtime, and can be injected into our
front-end code later on.

#### Fields
These are required to connect to your specific Watson Tone Analyser service.

- `apiKey` : IBM Cloud uses Identity and Access Management (IAM) - passing in the value of your API key enables you to access your service instance.
- `version` : Specifies a version of the Tone Analyser to use. The version passed will either be from the date specified, or the    newest version before that date. The current version is 27-09-21.
- `endpoint` : The location where your Watson Tone Analyser service instance is hosted.
- `toneAnalyser` : an instance of the ToneAnalyser type. This will be initialised in the authenticate method and used when analysing input.

#### Authenticate Method
Now that we have the data required to authenticate with our Watson Tone Analyser service, you need to initialise your `toneAnalyser` object to point to your Watson Tone Analysis service instance.

```Java
@PostConstruct
public void authenticate() {
  //sign into IAM with the api key
  IamOptions options = new IamOptions.Builder().apiKey(apiKey).build();

  //create an instance of the tone anaylser with your credentials and current version.
  this.toneAnalyser = new ToneAnalyzer(version, options);

  //points where your instance is hosted
  toneAnalyser.setEndPoint(endpoint);

}
```

- `@PostConstruct` is a CDI annotation indicates that this method will automatically be invoked as soon as our `Analyser` object is constructed, or injected into areas of our application, which we will explore later.

- The `IamOptions` object is created by passing in your API key into an IamOptions builder. This is used to access your specific Watson Tone service.

- Your `toneAnalyser` field is then initialised to a newest version of your specific Tone Analyser service, by creating a new `ToneAnalyzer` object, with the given `version` string (which refers to the latest version of the Tone Analyser), as well as your IAM access options.

- Your `toneAnalyser` object then needs its endpoint set to the location where your service is hosted. This is where it will send/receive requests to/from Watson.

#### AnalyseInput Method
Finally, you need a method to send your input data to Watson and receive an analysis of the input. To do this, you are going to create the analyse input method.
From the method signature, you can see that it takes your input sentence(s) to be analysed as a string argument, and returns an object with the type of `ToneAnalysis`.

```Java
public ToneAnalysis analyseInput(String input) {

		String text = input;

		// can be used to specify searching for specific tones, etc.
		ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();

		// stores the results of tone analysis
		ToneAnalysis toneAnalysis = null;

		try {
			// Invoke the Tone Analyser method, with the given options
			toneAnalysis = toneAnalyser.tone(toneOptions).execute().getResult();

		} catch (NotFoundException e) {
			// Handle Not Found (404) exception
			System.out.println("Service not found :" + e.getMessage());

		} catch (RequestTooLargeException e) {
			// Handle Request Too Large (413) exception
			System.out.println("Service returned status code " + e.getStatusCode() + ":" + e.getMessage());
		} catch (ServiceResponseException e) {
			// Base class for all exceptions caused by error responses from the service
			System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
		}

		return toneAnalysis;

	}
```

- A `ToneOptions` object is created, which acts as configuration for the tone analysis. Here, we chose to accept input in **text** form.
  From the `ToneOptions.Builder()`, you can specify the language of the input text, the type of the input (JSON, HTML, etc.), and much more. Check the [documentation](https://cloud.ibm.com/apidocs/tone-analyzer?code=java) for more information.

- A `ToneAnalysis` object is then created. This is used to hold data returned from a call to the `ToneAnalyzer`. The returned data contains a `DocumentAnalysis` object, a list of identified tones with scores over 0.5 for the entire input, and a `SentenceAnalysis` object, a list of each sentence, and it’s tones identified with scores over 0.5.

- The Watson Tone Analyser service returns HTTP response codes to indicate success or failure. Note the `Request Too Large (413)` exception - the example application is designed for relatively small input. If you want to process larger input, use the POST method, described in [the documentation](https://cloud.ibm.com/docs/services/tone-analyzer?topic=tone-analyzer-utgpe#using-the-general-purpose-endpoint)

#### Update server.xml

For those unfamiliar with the server.xml file, this contains configuration data about your server, such as the name, description, Java EE features enabled on the server, and ports which the server runs on.

Open Liberty is a lightweight Java application server. This means that when we deploy an application, we do not need to include every single feature of the Java EE language.
For example, if our application only consists of REST calls, then we should only package the JAX-RS feature into our server, rather than including servlets and other features of the Java EE language we don't use in our application. Think of these as as imports in a Java class - you don't keep unused imports in your class, so why keep unused features on your server?

Since we're using CDI, you need to add this feature to our Open Liberty server. Navigate to `src/main/liberty/config/server.xml`.

```xml
<server description="Open Liberty Tone Analyser">
    <featureManager>
        <feature>servlet-4.0</feature>
        <feature>cdi-2.0</feature> <!-- Add this feature! -->
        <feature>jsp-2.3</feature>
    </featureManager>

    <httpEndpoint httpPort="${default.http.port}" httpsPort="${default.https.port}" id="defaultHttpEndpoint"  host="*" />

    <webApplication id="ToneAnalyser" location="ToneAnalyser.war" contextRoot="${app.context.root}" />
</server>
```

You may also notice that we've included the `servlet-4.0` and `jsp-2.3` features. These are required, and will be explained later. We've added them so you don't have to.


## Step 4 - Creating the Front End

Now that you have the logic in place to authenticate, and analyse text with your Watson Tone Analyser instance, it would be nice to display this information to a webpage.

A way of connecting Java backend to a front-end webpage in Open Liberty can be done using servlets. If you are not familiar with servlets, a brief explanation is that they can be used to interact with a back-end Java server, by receiving and responding to HTTP requests from front-end web clients.

#### The Webpage

Within our application, we will use a `.jsp` (JavaServerPage) file for our front-end. For reference, a `.jsp` file is a HTML webpage which contains Java code and is parsed as a HTML file by the server. It enables us to add complex logic into HTML webpages - for example, dynamically creating a webpage based on the content returned from a call to your Java server. You can learn more about `.jsp` files [here](http://www.jsptut.com/). Don’t worry, we won't make you write it - the `index.jsp` file is provided for you at `src/main/webapp/` directory, and the feature for `.jsp` files is already included in your `server.xml`.

Here's the snippet of the `index.jsp` file, which makes sends our input to the Watson service:

```html
<form action="analyseinput" autocomplete="off" method="GET">
  Enter your text:<br /> <input type="text" name="inputText" /> <br />
  <p>Click this button to analyse your text with Watson.</p>
  <input type="submit" value="Submit" /> <br />
</form>
```

As you can see from the form attributes, submitting this form calls a **GET** request to the `/analyseinput` endpoint. However, we now need a servlet to handle this request from the web page, as
this endpoint does not exist at the moment.


#### The Servlet
So, you have a webpage, which makes requests to an endpoint, once this endpoint is hit, we want to call our `Analyser` class’ methods to call your Watson Tone Analyser service instance.

Create a class called `ToneServlet`, in the directory `src/main/java/io/openliberty/guides/watson/toneanalyser/`

```java
/**
 * Servlet class, handling get/post requests towards the Watson
 * Tone Analyser service, from the webpage.
 *
 *	@author Scott C Curtis
 */
@WebServlet("analyseinput/")
public class ToneServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	Analyser toneAnalyser;

	/**
	 * Handles a GET request from the .jsp frontend, to the Watson service.
	 *
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        //To be completed
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
```

### Code Walkthrough

#### Class Annotation
`@WebServlet(url)` identifies the endpoint the servlet is served at.

#### Fields
These are required to connect to your specific Watson Tone Analyser service.

- `toneAnalyser` : The injected CDI object of your `Analyser` class.
- `serialVersionUID` : This is required as servlet is a serialisable class. Don't worry about this.

#### doGet Method

`doGet()` is a method which overrides the default `doGet()` method of the servlet. This is called when the endpoint,
specified on the class annotation, is called with a HTTP **GET** method. When this is done, we want to our `Analyser` to
call the `analyseInput` method we defined earlier.

```Java
protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

  //takes the input string from the frontend form
  String input = request.getParameter("inputText");

  //returns the analysis of the input text
  ToneAnalysis analysis = toneAnalyser.analyseInput(input);

  //analysis object is now available as an attribute on frontend
  request.setAttribute("toneResponse", analysis);

  RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");

  rd.forward(request, response);

}
```

- First, the input from the form is read through the `request` made by the webpage.

- The input is then passed to and analysed by your `analyseInput()` method, and the tone analysis is returned into a `ToneAnalysis` object.

- The data returned from the tone analysis is assigned to a HTML attribute, `toneResponse`.

- The `toneResponse` attribute is then made available, through forwarding it back to `index.jsp`.



Usually, we would have to add the repository containing the servlet libraries in our `build.gradle` file, and update the `server.xml` to enable the Java servlet feature. Although, since you updated the `build.gradle` file earlier for Watson libraries, and updated the `server.xml` to include the CDI feature, the necessary dependencies have been included for you.


## Step 5 - Connecting and Configuring the Front-End

The final thing to do, is make the server aware of your servlet and webpage. This has already been done for you, in the `web.xml` file, located at `src/main/webapp/`.

```html
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
    <servlet>
		   <servlet-name>ToneServlet</servlet-name>
		   <servlet-class>io.openliberty.guides.watson.toneanalyser.ToneServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		  <servlet-name>ToneServlet</servlet-name>
		  <url-pattern>/analyseinput</url-pattern>
	</servlet-mapping>
</web-app>
```

- **welcome-file**: This is the file to be displayed at the default endpoint, which is `http://localhost:9080/ToneAnalyser`.
- **servlet**: Any servlets to be served by the application are defined like this, they are assigned a name, and the path to the class is associated with the servlet.
- **servlet-mapping**: This maps a servlet to a URL, by taking the servlet name defined in the **servlet** block, and a url pattern, affixed to the end of the default endpoint. In this case, it's http://localhost:9080/ToneAnalyser/anaylseinput.

## Step 7 - Building and Running the ApplicationScoped
The application is now complete, so navigate to the `start/` directory, and execute the following command to give yourself permission to run Gradle, then execute the following Gradle commands to **build** and **launch** the application on **Open Liberty**:

```
chmod a+x gradlew
./gradlew clean build
./gradlew libertyRun
```

You will find your application at the http://localhost:9080/ToneAnalyser/ endpoint. Navigate to this in a browser.

Once you're done, close the server with:

```
./gradlew libertyStop
```

Congratulations, you've successfully deployed **IBM Watson Tone Analyser** on **Open Liberty**!
