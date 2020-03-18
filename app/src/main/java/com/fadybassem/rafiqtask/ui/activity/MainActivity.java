package com.fadybassem.rafiqtask.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fadybassem.rafiqtask.R;
import com.fadybassem.rafiqtask.data.models.LocationRequestModel;
import com.fadybassem.rafiqtask.data.remote.ApiClient;
import com.fadybassem.rafiqtask.data.remote.ApiInterface;
import com.fadybassem.rafiqtask.data.remote.pojo.LocationDataModel;
import com.fadybassem.rafiqtask.data.remote.pojo.LocationModel;
import com.fadybassem.rafiqtask.ui.adapters.LocationAdapter;
import com.fadybassem.rafiqtask.ui.dialog.LocationDialog;
import com.fadybassem.rafiqtask.utils.WrapContentLinearLayoutManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    private String LOG_TAG = "MainActivity";

    private TextInputEditText edittext;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerview;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    private Dialog dialog, locationDialog;
    private ApiInterface apiInterface;

    private LocationAdapter adapter;

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = findViewById(R.id.edittext);
        progressBar = findViewById(R.id.progressBar);
        toggleButton = findViewById(R.id.toggleButton);

        progressBar.setVisibility(View.INVISIBLE);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                ActivityCompat.requestPermissions
                        (MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_RECORD_PERMISSION);
            } else {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.INVISIBLE);
                speech.stopListening();
            }
        });

        //showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        edittext.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
        text = text.replace(".", "");
        edittext.setText(text);
        showDialog();
//        new GetData(this, text).execute();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.show();
    }

    private void showDialog() {
        locationDialog = LocationDialog.getInstance().showDialog(this, query -> {
            getData(query);
        });

        recyclerview = locationDialog.findViewById(R.id.recyclerview);

    }

    private void getData(String query) {
        showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LocationModel> call = apiInterface.LOAD_HOME_MODEL_CALL(query);
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        set_recyclerview(response.body().getData());
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                dialog.dismiss();
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });
    }

    private void set_recyclerview(List<LocationModel.Datum> list) {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        if (adapter == null) {
            adapter = new LocationAdapter(list, datum -> {
                locationDialog.dismiss();
                getLocationData(datum);
            });
            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setAdapter(adapter);
            recyclerview.addItemDecoration(itemDecorator);
        }
    }

    private void getLocationData(LocationModel.Datum datum) {
        showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LocationDataModel> call = apiInterface.LOCATION_DATA_MODEL_CALL(datum.getId(), datum.getProvider(), datum.getAddressLine1(), datum.getAddressLine2());
        call.enqueue(new Callback<LocationDataModel>() {
            @Override
            public void onResponse(Call<LocationDataModel> call, Response<LocationDataModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        createCashKey(response.body().getData());
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LocationDataModel> call, Throwable t) {
                dialog.dismiss();
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });
    }

    private void createCashKey(LocationDataModel.Data data) {
        try {
            LocationRequestModel locationReqestModel = new LocationRequestModel(data.getAddress().getAddress1(), data.getReference(), data.getReferenceType(), String.valueOf(data.getLatitude()), String.valueOf(data.getLongitude()), new ArrayList());

            //String pre = locationReqestModel.toString(locationReqestModel) /*+ "?BTDXO[]"*/;

            //String pre = "{\"address\":\"" + data.getAddress().getAddress1() + "\",\"reference\":\"" + data.getReference() + "\",\"referenceType\":\"" + data.getReferenceType() + "\",\"latitude\":" + data.getLatitude() + ",\"longitude\":" + data.getLongitude() + ",\"delivery\":" + "[]}";

            String pre = "{\"address\":\"" + data.getAddress().getAddress1() + "\",\"reference\":\"" + data.getReference() + "\",\"referenceType\":\"" + data.getReferenceType() + "\",\"latitude\":" + data.getLatitude() + ",\"longitude\":" + data.getLongitude() + "}";

            String encodedurl = URLEncoder.encode(pre, "UTF-8");

            byte[] bytes = encodedurl.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);

            Log.e(LOG_TAG, base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}