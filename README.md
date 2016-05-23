SociaLogin sample application allows you to login with social network Twitter and Google.
The app uses two helper classes to handle login process:
GoogleAgent
TwitterAgent

To use these classes all you have to do is to instantiate objects of the classes in your activity passing the activity to it.
        
    googleAgent = new GoogleAgent(this/*AppCompatActivity*/);
    twitterAgent = new TwitterAgent(this/*Activity*/);
        
Then you can simply call login method
        
    	googleAgent.login();
        	TwitterAgent.login();

Then you have to implement onActivityResult() passing its parameters to the helper class's onActivityResult method
    	GoogleAgentAgent.onActivityResult(data);
   	 twitterAgent.onActivityResult(requestCode, resultCode, data);
        
if the login sucesseded the helper class returns an object that contains the data of the current user
but it differs between the two classes, the googleAgent class returns account object from its onActivityResult while the TwitterAgent returns a user object from its login method. That's because on google login we start the login activity manually and then we launch the detail activity from the Main Activity's on onActivityResult:
	Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
  mActivity.startActivityForResult(signInIntent, GOOGLE_SIGNIN_CODE);

While in the Twitter agent the TwitterAuthClient do the job for us and returns a user object as a result from its Callback. 
For google it is called GoogleSignInAccount
             
https://developers.google.com/android/reference/com/google/andr 
oid/gms/auth/api/signin/GoogleSignInAccount#inherited- 
constant-summary   
            we can use its getters to get user's data
            String	    getDisplayName()
            String	    getEmail()
            String	    getId()
            Uri	    getPhotoUrl()
        
        and for twitter it is called User
	the User's fields can be accessed directly

long	id	The integer representation of the unique identifier for this User.

String	name	The name of the user, as they defined it.

String	profileImageUrl		A HTTP-based URL pointing to the user avatar image.
String	location 	Nullable	
String	email 		Nullable	requires additional permission*

To get the current user email we have to get two permissions one from the user and  the other from Twitter itself.
1.	Request permission from the user:
To request a user’s email, call the TwitterAuthClient#requestEmail method. This will start the ShareEmailActivity and display the "share your email address" screen to the user, If the user grants access and the email address is available, the success method is called with the email address in the result.
2.	Request permission from Twitter
Requesting a User’s Email Address requires application to be whitelisted by Twitter. To request access, visit https://support.twitter.com/forms/platform.
Then choose " I need access to special permissions" and fill the required fields and submit, and pray for twitter's approval email.
References
The following guides and tutorials from developer.anroid, fabric.io and other gives me a lot of help and step by step guidance to build this app.

1.Setting Up Google Play Services
https://developers.google.com/android/guides/setup#add_google_play_services_to_your_project

2.Start integrating Google+ into your Android app
https://developers.google.com/+/mobile/android/getting-started

3.Accessing Google APIs
https://developers.google.com/android/guides/api-client#start_an_automatically_managed_connection


4. Install twitter via gradle
https://fabric.io/kits/android/twitterkit/install

5. Authentication with twitter and fabric
http://code.tutsplus.com/tutorials/quick-tip-authentication-with- twitter-and-fabric--cms-23801
6.
 https://docs.fabric.io/javadocs/twitter-core/1.4.3/com/twitter/sdk/android/core/models/package-tree.html
7. https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInAccount
  Issues
If you choose not to authorize the app with twitter, the app returns to the main activity but when you click on the login with twitter again TwitterAuthException is thrown, you have to restart the app to log in (working on it).
