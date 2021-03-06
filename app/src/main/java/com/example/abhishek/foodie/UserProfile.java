package com.example.abhishek.foodie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;

@SuppressWarnings("ALL")
public class UserProfile extends AppCompatActivity {
    //0 for breakfast, 1 for lunch, 2 dinner ,3 for special item.
    TextView[] price_textview;
    TextView user_display_name;
    static final String PREF_FILE_NAME = "my_price_file";
    static User current_user;
    FoodMenuItem foodTaken;
    static Context context;
    Button button_start_transaction;
    boolean is_photo_taken;
    float[] prices;
    TextView[] plus_button;
    TextView[] minus_button;
    static String user_image;     //base64 string
    int[] counter;
    Thread picture_taking_thread;
    FrameLayout user_image_preview;
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 15, bos);
            byte[] b = bos.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
            user_image_preview.setBackground(new BitmapDrawable(bmp));
            user_image = Base64.encodeToString(b, Base64.DEFAULT);
            System.out.println("ImageString" + user_image);
            /*

            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }   */
        }
    };
    private Camera mCamera;
    private CameraPreview mCameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page);
        is_photo_taken = false;
        takePicture();
        context=this;
        int user_index = getIntent().getIntExtra("Position", 0);
        current_user = MainActivity.list_of_all_users.get(user_index);
        user_display_name = (TextView) findViewById(R.id.user_name);
        price_textview = new TextView[4];
        price_textview[0] = (TextView) findViewById(R.id.tv_0);
        price_textview[1] = (TextView) findViewById(R.id.tv_1);
        price_textview[2] = (TextView) findViewById(R.id.tv_2);
        price_textview[3] = (TextView) findViewById(R.id.tv_3);
        button_start_transaction = (Button) findViewById(R.id.bt_confirm_transaction);
        //initializing the counters
        plus_button = new TextView[4];
        plus_button[0] = (TextView) findViewById(R.id.plus0);
        plus_button[1] = (TextView) findViewById(R.id.plus1);
        plus_button[2] = (TextView) findViewById(R.id.plus2);
        plus_button[3] = (TextView) findViewById(R.id.plus3);
        minus_button = new TextView[4];
        minus_button[0] = (TextView) findViewById(R.id.minus0);
        minus_button[1] = (TextView) findViewById(R.id.minus1);
        minus_button[2] = (TextView) findViewById(R.id.minus2);
        minus_button[3] = (TextView) findViewById(R.id.minus3);
        initPage();
        refresh_price_value();
    }

    void refresh_price_value() {
        for (int i = 0; i < 4; i++) {
            p("Item:" + counter[i]);
            p("New price:" + Float.toString(prices[i] * counter[i]));
            price_textview[i].setText("Rs " + Float.toString(prices[i] * counter[i]));
        }
    }

    void p(String str) {
        System.out.println("" + str);
    }

    @Override
    public void onBackPressed() {
        boolean success = false;
        while (!success) {
            try {
                picture_taking_thread.join();
                success = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();
    }

    void initPage() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        prices = new float[4];

        prices[0] = sharedPreferences.getFloat("breakfast", 0.0f);
        prices[1] = sharedPreferences.getFloat("lunch", 0.0f);
        prices[2] = sharedPreferences.getFloat("dinner", 0.0f);
        prices[3] = sharedPreferences.getFloat("special_item", 0.0f);
        user_display_name.setText(current_user.user_name);
        //TODO: start taking photo activity
        //TODO: set the profile picture fetched from the database

        button_start_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(UserProfile.this, "Loading", "Taking photo,please wait", true);
                progressDialog.setCancelable(false);

                picture_taking_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (UserProfile.user_image == null) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.cancel();
                    }
                });
                picture_taking_thread.start();
                for (int i = 0; i < 4; i++) {
                    if (counter[i] > 0) {
                        //creating a new transaction
                        Transaction t = new Transaction();
                        t.transaction_id = System.currentTimeMillis();
                        t.user_id = current_user.user_id;
                        t.is_guest = current_user.is_guest;
                        t.setFoodType(i);
                        t.price = prices[i] * counter[i];
                        t.time_stamp = new Date();
                        t.user_img = user_image;
                        //TODO: get today's date dd/mm/yyyy format string.
                        DataToWeb.sendTransaction(t);
                    }
                }
                //get the user id.
                //get the type of food taken
                //get the number of items taken
                //if food items taken are more than 1, do more transaction.
                //add the transaction to the send queue.
                //TODO: make it fail-safe...
                //TODO:handle the case when internet is not connected.
                //show a progress bar.
                //TODO: wait till photo is taken

            }
        });
        //init item counters
        counter = new int[4];
        //TODO: extension::
        //TODO: based on the time of the day, increase one counter and leave others ZERO.
        for (int i = 0; i < 4; i++)
            counter[i] = 0;

        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            plus_button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p("changing the price");
                    counter[finalI]++;
                    refresh_price_value();
                }
            });
        }
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            minus_button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counter[finalI] > 0)
                        counter[finalI]--;
                    refresh_price_value();
                }
            });
        }

    }

    //Adding Camera Functions
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

        } catch (Exception e) {
            // cannot get camera or does not exist

            //Toast.makeText(,"Error Opening Front Camera","Cannot open front Camera").show();
        }
        return camera;
    }

    void takePicture() {
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        user_image_preview = (FrameLayout) findViewById(R.id.preview);
        user_image_preview.addView(mCameraPreview);
        Toast.makeText(UserProfile.this, "Please Adjust your face for Photoclick ", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                try {
                    if (mCamera != null) {
                        mCamera.takePicture(null, null, mPicture);
                        is_photo_taken = true;
                        Toast.makeText(UserProfile.this, "Picture Taken", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 6000);


    }

}