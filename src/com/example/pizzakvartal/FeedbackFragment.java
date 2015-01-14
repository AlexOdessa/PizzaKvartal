package com.example.pizzakvartal;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.pizzakvartal.OrderFragment.SendOrder;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class FeedbackFragment extends Fragment {
	
	String[] data = {"Отзыв", "Предложение", "Жалоба"};
	boolean threadRunning;
	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.feedback);
		}
		
		threadRunning=false;
		
		
	}

	
	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.feedback);
		}
	}



	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		View v = inflater.inflate(R.layout.feedbacklayout, container, false);
		
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     final Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
	     spinner.setPrompt("Тип сообщения");
	     spinner.setAdapter(adapter);
		
	     final EditText feedbackText=(EditText) v.findViewById(R.id.editText1);
	     
	     Button sendButton=(Button) v.findViewById(R.id.sendButton);
	     sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (feedbackText.getText().toString().trim().length() == 0){
					
					Toast toast = Toast.makeText(getActivity(),
							"Введите сообщение", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				if (!threadRunning) {

					// ---
					AlertDialog dialog;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("Пожалуйста подождите...");
					builder.setCancelable(false);
					ProgressBar pb = new ProgressBar(getActivity());
					builder.setView(pb);

					dialog = builder.create();
					dialog.show();
					
					// builder.show();
					new SendFeedback().execute(dialog, spinner.getSelectedItem().toString().concat("/").concat(feedbackText.getText().toString().trim()));
				}
				
				
				
			}
		});
		
		return v;
	}

	public static FeedbackFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		FeedbackFragment fragment = new FeedbackFragment();
		fragment.setArguments(arg);

		return fragment;
	}
	
	class SendFeedback extends AsyncTask<Object, Void, Void> {

		boolean status;
		AlertDialog dialog;
		String msg;
		
		@Override
		protected Void doInBackground(Object... params) {
			// TODO Auto-generated method stub
			dialog = (AlertDialog) params[0];
			msg=(String) params[1];
			threadRunning = true;
			status = false;

			
			msg = msg.concat("/").concat(
					(String) DateFormat.format("yyyy-MM-dd hh:mm:ss",
							System.currentTimeMillis())).concat("<br>");



			if (msg.length() > 0) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://italiankvartal.esy.es/pizzakvartalfeedback.php");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("id", "12345"));
					nameValuePairs.add(new BasicNameValuePair("message",
							URLEncoder.encode(msg, "UTF-8")));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httppost);

					HttpEntity resEntity = response.getEntity();

					if (resEntity != null) {

						if (response.getStatusLine().getStatusCode() == 200) {
							status = true;
						}

					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if (status) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Сообщение отправлено");
				builder.setPositiveButton("ОК", null);
				builder.setIcon(R.drawable.positive);
				builder.setMessage(R.string.sendPositiveFeedback);

				// builder.show();

				AlertDialog dialog = builder.show();
				TextView messageText = (TextView) dialog
						.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				dialog.show();
				// send2Arch.setVisibility(View.VISIBLE);

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Сообщение не отправлено");
				builder.setPositiveButton("ОК", null);
				builder.setIcon(R.drawable.negative);
				builder.setMessage(R.string.sendNegativeFeedback);

				// builder.show();

				AlertDialog dialog = builder.show();
				TextView messageText = (TextView) dialog
						.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				dialog.show();

			}
			threadRunning = false;

		}

	}
}
