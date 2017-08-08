package ivan.flashtorch;

import android.support.v7.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.provider.Settings;
import android.content.Context;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    private ImageButton flashLight;
    private ImageButton infinity;
    private ImageButton frontlight;
    private ImageButton blink;
    private int delay = 100;
    private int times = 3;
    private Camera camera;
    private Parameters parameter;
    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;
    private boolean isFlashLightOn1 = false;
    private boolean blinkonoff = false;
    private boolean on = false;
    private boolean blinks = false;
    private int Brightness;
    TextView progress;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        flashLight = (ImageButton)findViewById(R.id.flash_light);
        infinity = (ImageButton)findViewById(R.id.infinity);
        frontlight = (ImageButton)findViewById(R.id.frontscreen);
       // blink = (ImageButton)findViewById(R.id.blink);


      //  progress = (TextView)findViewById(R.id.textView2);


      //  progress.setText("Screen Brightness : " + Brightness);
        int curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS,-1);
        deviceHasFlash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!deviceHasFlash){
            Toast.makeText(MainActivity.this, "Sorry, you device does not have any camera", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            this.camera = Camera.open(0);
            parameter = this.camera.getParameters();
        }

        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFlashLightOn){
                    turnOnTheFlash();
                    infinityon();
                }else{
                    turnOffTheFlash();
                    infinityoff();
                }
            }
        });

        frontlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlashLightOn1) {
                    infinityoff();
                    turnOffTheFlash();
                    frontscreenOn();
                } else {

                    //turnOnTheFlash(); // need to check
                    frontscreenOff();


                }
            }
        });



    }


    private void infinityon(){
        infinity.setImageResource(R.drawable.infinityon);
    }
    private void infinityoff(){
        infinity.setImageResource(R.drawable.infinityoff);
    }
    private void frontscreenOn() { frontlight.setImageResource(R.drawable.frontscreenon);
        try {
            //sets manual mode and brightnes 255
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);  //this will set the brightness to maximum (255)

            //refreshes the screen
            int br = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = (float) br / 255;
            getWindow().setAttributes(lp);
            isFlashLightOn1 = true;
        } catch (Exception e) {}
        RelativeLayout mLinearLayout = (RelativeLayout) findViewById(R.id.backgroundid);
        mLinearLayout.setBackgroundResource(R.drawable.backgroundoff);
    }


    private void frontscreenOff() {  frontlight.setImageResource(R.drawable.frontscreenoff);

        frontlight.setImageResource(R.drawable.light);
        try {
            //sets manual mode and brightnes 255
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
          //  Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);  //this will set the brightness to maximum (255)

            //refreshes the screen
            int br = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
           // lp.screenBrightness = (float) br / 255;
            lp.screenBrightness = (float)  0;
            getWindow().setAttributes(lp);
            isFlashLightOn1 = false;
        } catch (Exception e) {}
        RelativeLayout mLinearLayout = (RelativeLayout) findViewById(R.id.backgroundid);
        mLinearLayout.setBackgroundResource(R.drawable.background);
    }


    private void turnOffTheFlash() {
        parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
        this.camera.setParameters(parameter);
        this.camera.stopPreview();
        isFlashLightOn = false;
        flashLight.setImageResource(R.drawable.buttonoff);
        infinity.setImageResource(R.drawable.infinityoff);
        blinks = false;

    }



    private void turnOnTheFlash() {
        if(this.camera != null){
            parameter = this.camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameter);
            this.camera.startPreview();
            isFlashLightOn = true;
            flashLight.setImageResource(R.drawable.buttonon);
            blinks = true;

        }
    }


    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameter = camera.getParameters();
            } catch (RuntimeException e) {
                System.out.println("Error: Failed to Open: " + e.getMessage());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(this.camera != null){
            this.camera.release();
            this.camera = null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        turnOffTheFlash();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(deviceHasFlash){
            turnOffTheFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }








}
