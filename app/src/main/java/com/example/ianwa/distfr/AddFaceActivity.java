package com.example.ianwa.distfr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import android.graphics.Bitmap;
import android.view.View;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import android.util.Base64;

import org.json.JSONObject;

public class AddFaceActivity extends AppCompatActivity {

    Button CaptureImageFromCamera,UploadImageToServer;
    ImageView ImageViewHolder;
    ProgressDialog progressDialog ;
    Intent intent ;
    public static final int RequestPermissionCode  = 1 ;
    Bitmap bitmap;
    boolean check = false;
    String GetImageNameFromEditText;
    String ImageUploadPathOnSever ="https://zeblin.bid/loadimage";
    String acc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_face);
        CaptureImageFromCamera = (Button)findViewById(R.id.addFace_take);
        ImageViewHolder = (ImageView)findViewById(R.id.addFace_iv);
        UploadImageToServer = (Button) findViewById(R.id.upload);
        EnableRuntimePermissionToAccessCamera();
        acc_id = getIntent().getStringExtra("acc_id");

        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView message = (TextView)findViewById(R.id.addFace_message);
                message.setText(getString(R.string.blank));
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

    }

    public void onUploadClicked(View view){
        EditText imageName = (EditText)findViewById(R.id.p_name);
        GetImageNameFromEditText = imageName.getText().toString();
        if(GetImageNameFromEditText.isEmpty()){
            TextView message = (TextView)findViewById(R.id.addFace_message);
            message.setText(getString(R.string.error_pers));
            return;
        }
        if(ImageViewHolder.getDrawable() == null){
            TextView message = (TextView)findViewById(R.id.addFace_message);
            message.setText(getString(R.string.error_img));
            return;
        }
        ImageUploadToServerFunction();
    }

    // Start activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK ) {
            try {
                // Adding captured image in bitmap.
                bitmap = (Bitmap) data.getExtras().get("data");
                // adding captured image in image view.
                ImageViewHolder.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("We getting caught up");
            }
        }
    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddFaceActivity.this,Manifest.permission.CAMERA)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(AddFaceActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddFaceActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    // Upload captured image online on server function.
    public void ImageUploadToServerFunction(){
        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.NO_WRAP);


        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(AddFaceActivity.this,"Image is Uploading","Please Wait",false,false);
            }
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.
                Toast.makeText(AddFaceActivity.this,string1,Toast.LENGTH_LONG).show();
                // Setting image as transparent after done uploading.
                ImageViewHolder.setImageResource(android.R.color.transparent);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<>();
                HashMapParams.put("image_name", "banana");
                /*HashMapParams.put("cmd", "addFace");
                HashMapParams.put("p_name", GetImageNameFromEditText);
                HashMapParams.put("acc_id", acc_id);*/
                HashMapParams.put("image_string", ConvertImage);
                return imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    private class ImageProcessClass{
        String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                JSONObject json = new JSONObject();
                URL url = new URL(requestURL);
                HttpsURLConnection httpURLConnectionObject = (HttpsURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                httpURLConnectionObject.setDoOutput(true);
                httpURLConnectionObject.setDoInput(true);
                for(Map.Entry<String, String> e: PData.entrySet()){
                    json.put(e.getKey(), e.getValue());
                }
                String data = json.toString();
                System.out.println(data);
                httpURLConnectionObject.setFixedLengthStreamingMode(data.getBytes().length);
                httpURLConnectionObject.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                OutputStream OutPutStream = httpURLConnectionObject.getOutputStream();
                BufferedWriter bufferedWriterObject = new BufferedWriter(new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(data);
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                httpURLConnectionObject.connect();

                int RC = httpURLConnectionObject.getResponseCode();
                System.out.println(RC);
                BufferedReader bufferedReaderObject ;
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                    System.out.println(stringBuilder.toString());
                }
                else if (RC >= 400 && RC < 500){
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getErrorStream()));
                    String RC2;
                    while((RC2 = bufferedReaderObject.readLine()) != null){
                        System.out.println(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        //old encoding
        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddFaceActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddFaceActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onBackClicked(View view){
        Intent intent= new Intent(this, AccountHomeActivity.class);
        intent.putExtra("acc_id", acc_id);
        startActivity(intent);
        finish();
    }
}
