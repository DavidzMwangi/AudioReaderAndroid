package ke.co.davidwanjohi.audioreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppCompatTextView audioFileName;
    AppCompatButton audioFileBtn;
    String selectedImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioFileName=(AppCompatTextView) findViewById(R.id.selected_audio_file);
        audioFileBtn=(AppCompatButton) findViewById(R.id.select_btn);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

//                getAllSongsFromSDCARD();





            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override public void onError(DexterError error) {
                        Log.e("Dexter", "There was an error: " + error.toString());
                    }
                }).check();


        audioFileBtn.setOnClickListener(l->{
            Intent videoIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), 1);

        });




    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){



                        Uri uri = data.getData();
                        try {
                            String uriString = uri.toString();
                            File myFile = new File(uriString);
                            //    String path = myFile.getAbsolutePath();

                            String path2 = getAudioPath(uri);

                            File f = new File(path2);
                            String displayName =f.getName();
                            long fileSizeInBytes = f.length();
                            long fileSizeInKB = fileSizeInBytes / 1024;
                            long fileSizeInMB = fileSizeInKB / 1024;

                            //ensure the file is not more than 20 mb
                            if (fileSizeInMB > 20) {

                                Toast.makeText(getApplicationContext(),"Can't Upload, sorry file size is large. Maximum file size is 20MB",Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(),path2,Toast.LENGTH_SHORT).show();
                                audioFileName.setText(displayName);
//                                profilePicUrl = path2;
//                                isPicSelect = true;
                            }
                        } catch (Exception e) {
                            //handle exception
                            Toast.makeText(getApplicationContext(), "Unable to process,try again", Toast.LENGTH_SHORT).show();
                        }
                        //   String path1 = uri.getPath();


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
