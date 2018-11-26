package no.hiof.joakimj.remmenproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://81.166.82.90/imageuploader.php";
    public static final String UPLOAD_KEY_IMAGE = "image";
    public static final String UPLOAD_KEY_FOODNAME = "foodname";
    public static final String UPLOAD_KEY_ALLERGIER = "allergier";
    public static final String UPLOAD_KEY_USER_ID = "user_id";
    public static final String UPLOAD_KEY_COMMENT = "comment";
    public static final String UPLOAD_KEY_DESCRIPTION = "description";

    private int PICK_IMAGE_REQUEST = 1;

    private Button btnUploadRecipe, btnViewImage, btnChooseImage;
    private CheckBox cB1,cB2, cB3, cB4, cB5, cB6, cB7, cB8, cB9, cB10, cB11, cB12, cB13, cB14, cB15;
    private EditText nameEditText, commentEditText, descriptionEditText;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        btnChooseImage = findViewById(R.id.buttonChoose);
        btnUploadRecipe = findViewById(R.id.buttonUpload);
        btnViewImage = findViewById(R.id.buttonViewImage);

        nameEditText = findViewById(R.id.name_edit_text);
        commentEditText = findViewById(R.id.comment_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);

        imageView = findViewById(R.id.imageView);

        cB1 = findViewById(R.id.checkBox1);
        cB1.setText(R.string.shellfish);
        cB2 = findViewById(R.id.checkBox2);
        cB2.setText(R.string.lactose_milk);
        cB3 = findViewById(R.id.checkBox3);
        cB3.setText(R.string.egg);
        cB4 = findViewById(R.id.checkBox4);
        cB4.setText(R.string.peanutt);
        cB5 = findViewById(R.id.checkBox5);
        cB5.setText(R.string.gluten_wheat);
        cB6 = findViewById(R.id.checkBox6);
        cB6.setText(R.string.soy);
        cB7 = findViewById(R.id.checkBox7);
        cB7.setText(R.string.fish);
        cB8 = findViewById(R.id.checkBox8);
        cB8.setText(R.string.lupine);
        cB9 = findViewById(R.id.checkBox9);
        cB9.setText(R.string.nuts);
        cB10 = findViewById(R.id.checkBox10);
        cB10.setText(R.string.celery);
        cB11 = findViewById(R.id.checkBox11);
        cB11.setText(R.string.mustard);
        cB12 = findViewById(R.id.checkBox12);
        cB12.setText(R.string.molluscs);
        cB13 = findViewById(R.id.checkBox13);
        cB13.setText(R.string.sulfur);
        cB14 = findViewById(R.id.checkBox14);
        cB14.setText(R.string.meat_mammal);
        cB15 = findViewById(R.id.checkBox15);
        cB15.setText("not yet implemented");

        btnChooseImage.setOnClickListener(this);
        btnUploadRecipe.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnChooseImage) {
            showFileChooser(); 
        }
        if(v == btnUploadRecipe) {
            uploadImage();
            uploadRecipe();

        }
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddRecipeActivity.this, "Upload Recipe", "Please wait . . . ", true, true);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                Log.i("TAG", "Bøkde:"+uploadImage);
                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY_IMAGE, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                if(!s.equals("failed")){
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Noe gikk galt", Toast.LENGTH_SHORT);
                }


            }

        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    private void uploadRecipe() {
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
