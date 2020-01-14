package com.example.project3_test1.HomeFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.project3_test1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private View v;

    private ImageButton cameraButton;
    GridLayout gridLayout;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    private File photoFile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        cameraButton = v.findViewById(R.id.cameraBtn);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        gridLayout = v.findViewById(R.id.gridLayout);
        setSingleEvent(gridLayout);

        return v;
    }

    private void setSingleEvent(GridLayout gridLayout) {
        for (int i = 0; i<gridLayout.getChildCount();i++)
        {
            CardView CardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalI) {
                        case 0:
                            Intent intent0 = new Intent(getContext(), PlasticActivity.class);
                            startActivity(intent0);
                            break;
                        case 1:
                            Intent intent1 = new Intent(getContext(), VinylActivity.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(getContext(), CanActivity.class);
                            startActivity(intent2);
                            break;
                        case 3:
                            Intent intent3 = new Intent(getContext(), PaperActivity.class);
                            startActivity(intent3);
                            break;
                        case 4:
                            Intent intent4 = new Intent(getContext(), GlassActivity.class);
                            startActivity(intent4);
                            break;
                        case 5:
                            Intent intent5 = new Intent(getContext(), PaperPackActivity.class);
                            startActivity(intent5);
                            break;

                    }

//                    Toast.makeText(getContext(), "Clicked" + finalI, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            Bitmap btm = null;
            Bitmap btm2 = null;

            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);

            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if(exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
                btm = exif.getThumbnailBitmap();
            } else {
                exifDegree = 0;
            }
            btm2 = rotate(btm, exifDegree);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float) (1024/(float) btm2.getWidth());
            int image_w = (int) (btm2.getWidth() * scale);
            int image_h = (int) (btm2.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(btm2, image_w, image_h, true);
            btm2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
            byte[] bytes = stream.toByteArray();

            Intent intent = new Intent(getActivity(), HomeTrashPhotoActivity.class);
            intent.putExtra("BitmapImage", bytes);
            //startActivity(intent);
            //((HomeTrashPhotoActivity) getActivity()).setTrashImage(rotate(bitmap, exifDegree));
            startActivity(intent);

            //((ImageView) v.findViewById(R.id.photo)).setImageBitmap(rotate(bitmap, exifDegree));
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(getView().getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(photoFile != null) {
                if (photoFile.exists()) {
                    if (photoFile.delete()) {
                        photoFile = null;
                    }
                }
            }

            return;
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(v.getContext().getPackageManager()) != null) {
//            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {

            }

            if(photoFile != null) {
                photoUri = FileProvider.getUriForFile(v.getContext(), v.getContext().getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = v.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

}
