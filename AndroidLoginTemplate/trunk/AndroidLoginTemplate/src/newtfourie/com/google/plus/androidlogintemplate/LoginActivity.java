package newtfourie.com.google.plus.androidlogintemplate;

import newtfourie.com.google.plus.androidlogintemplate.common.CommonAndroidTasks;
import newtfourie.com.google.plus.androidlogintemplate.login.ManageLogin;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		//TODO remove this email
		mEmail = "android@template.login";
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//Check if the user is already logged in
		if (!ManageLogin.getInstance(getApplicationContext()).mustLogin())
		{
			finish();
			ManageLogin.getInstance(getApplicationContext()).dispose();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		//Clean up memory lying around
		ManageLogin.getInstance(getApplicationContext()).dispose();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.login_error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.login_error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.login_error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		//Either display the loading icon or not
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute(new String[] {mEmail, mPassword});
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, Void, String[]> {
		@Override
		protected String[] doInBackground(String... params) {
			//Get information from call
			String username = params[0];
			String password = params[1];
			
			// TODO attempt authentication against a network service.

			try {
				// Simulate network access.
				//TODO Remove this thread sleep once we actually connect to the internet to verify creditinals
				Thread.sleep(2000);
				
				//TODO remove this check on login credentials
				if (password.equals("123") && username.equals("android@template.login"))
				{
					return new String[] {"true", username, password};
				}
				else
				{
					//TODO register the new account here. (if required)
					return new String[] {"false"};
				}
				//TODO this exception will change
			} catch (InterruptedException e) {
				return new String[] {"false"};
			}
		}

		@Override
		protected void onPostExecute(final String[] result) {
			mAuthTask = null;
			showProgress(false);

			String strSuccess = result[0];
			boolean success = strSuccess.equals("true");
			
			if (success) {
				//save logged in user
				String username = result[1];
				String password = result[2];
				
				ManageLogin.getInstance(getApplicationContext()).saveLoggedInUser(new String[] {username, password});
				
				//Go to home screen
				CommonAndroidTasks.SimpleActivitySwitch.mainScreen(LoginActivity.this);
				
				//close this activity
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.login_error_unable_to_login));
				mPasswordView.requestFocus();
				
				CommonAndroidTasks.toastLong(LoginActivity.this, getString(R.string.login_error_unable_to_login));
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
			
		}
	}
}
