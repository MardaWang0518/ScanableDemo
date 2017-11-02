package com.mardawang.android.scanabledemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE = 99;
    private static final int SCAN_CODE = 1024;

    private Button btn_scan;
    private Button btn_media;
    private Button btn_camera;
    private ImageView scannedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        scannedImage = (ImageView) findViewById(R.id.scannedImage);

        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_media = (Button) findViewById(R.id.btn_media);
        btn_media.setOnClickListener(this);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera:
                startScan(ScanConstants.OPEN_CAMERA);
                break;
            case R.id.btn_media:
                startScan(ScanConstants.OPEN_MEDIA);
                break;
            case R.id.btn_scan:
                Toast.makeText(this,"敬请期待！",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, CaptureActivity.class);
//                startActivityForResult(intent, SCAN_CODE);
                break;
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Log.d("test_data",data.toString());
            if (requestCode == REQUEST_CODE ) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    getContentResolver().delete(uri, null, null);
                    scannedImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == SCAN_CODE && data!=null ){
                String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                Bitmap bmp = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);
                Log.d("test_result",result.toString());
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
//                if(result.contains("https://")){
//                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(result));
//                    //建立Intent对象，传入uri
//                    startActivity(intent);
//                }else{
//                    scannedImage.setImageBitmap(bmp);
//                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
