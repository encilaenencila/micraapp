package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;


public class FaceRecognition extends AppCompatActivity {
    private static final String TAG = "FaceRecognition";
    private static final int PERMISSION_CODE = 1001;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private PreviewView previewView;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private GraphicOverlay graphicOverlay;
    private TextView detectionTextView;

    private Interpreter tfLite;
    private boolean flipX = false;
    private boolean start = true;
    private String IntentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        previewView = findViewById(R.id.previewView);
        previewView.setScaleType(PreviewView.ScaleType.FIT_CENTER);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        detectionTextView = findViewById(R.id.detection_text);


        loadModel();


        IntentValue = getIntent().getExtras().getString("type", "");


    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    private void getPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (requestCode == PERMISSION_CODE) {
            setupCamera();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startCamera() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            setupCamera();
        } else {
            getPermissions();
        }
    }

    private void setupCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindAllCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "cameraProviderFuture.addListener Error", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }
        Preview.Builder builder = new Preview.Builder();
        previewUseCase = builder.build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());
        try {
            cameraProvider
                    .bindToLifecycle(this, cameraSelector, previewUseCase);
        } catch (Exception e) {
            Log.e(TAG, "Error when bind preview", e);
        }
    }

    private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }

        Executor cameraExecutor = Executors.newSingleThreadExecutor();

        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();

        analysisUseCase = builder.build();
        analysisUseCase.setAnalyzer(cameraExecutor, this::analyze);

        try {
            cameraProvider
                    .bindToLifecycle(this, cameraSelector, analysisUseCase);
        } catch (Exception e) {
            Log.e(TAG, "Error when bind analysis", e);
        }
    }


    @SuppressLint("UnsafeOptInUsageError")
    private void analyze(@NonNull ImageProxy image) {
        if (image.getImage() == null) return;

        InputImage inputImage = InputImage.fromMediaImage(
                image.getImage(),
                image.getImageInfo().getRotationDegrees()
        );

        FaceDetector faceDetector = FaceDetection.getClient();

        faceDetector.process(inputImage)

                .addOnSuccessListener(faces -> onSuccessListener(faces, inputImage))
                .addOnFailureListener(e -> Log.e(TAG, "Barcode process failure", e))
                .addOnCompleteListener(task -> image.close());

    }

    private void onSuccessListener(List<Face> faces, InputImage inputImage) {
        Rect boundingBox = null;
        String name = null;
        float scaleX = (float) previewView.getWidth() / (float) inputImage.getHeight();
        float scaleY = (float) previewView.getHeight() / (float) inputImage.getWidth();


        if (faces.size() > 0) {
            Face face = faces.get(0);
            // get bounding box of face;
            boundingBox = face.getBoundingBox();
            int left = boundingBox.left;
            int top = boundingBox.top;
            int right = boundingBox.right;
            int bottom = boundingBox.bottom;

            Log.i("left", "" + left);
            Log.i("top", "" + top);
            Log.i("right", "" + right);
            Log.i("bottom", "" + bottom);

            if (left >= 15 && left < 26 && top >= 135 && top < 150 && right >= -430 && right < 450 && bottom >= 555 && bottom < 565) {
                detectionTextView.setText(R.string.face_detected);
                switch (IntentValue) {
                    case "waste":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent;
                        Intent = new Intent(FaceRecognition.this, WasteActivity.class);
                        startActivity(Intent);
                        cameraProvider.unbindAll();
                        finish();
                        break;
                    case "waste2":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent1;
                        Intent1 = new Intent(FaceRecognition.this, WasteActivity3.class);
                        startActivity(Intent1);
                        cameraProvider.unbindAll();
                        finish();
                        break;
                    case "infra1":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent2;
                        Intent2 = new Intent(FaceRecognition.this, InfraActivity.class);
                        startActivity(Intent2);
                        cameraProvider.unbindAll();
                        finish();
                        break;
                    case "infra2":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent3;
                        Intent3 = new Intent(FaceRecognition.this, InfraActivity3.class);
                        startActivity(Intent3);
                        cameraProvider.unbindAll();
                        finish();
                        break;
                    case "crime1":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent4;
                        Intent4 = new Intent(FaceRecognition.this, CrimeActivity.class);
                        startActivity(Intent4);
                        cameraProvider.unbindAll();
                        finish();
                        break;
                    case "crime2":
                        SaveInternal(previewView.getBitmap());
                        Intent Intent5;
                        Intent5 = new Intent(FaceRecognition.this, CrimeActivity3.class);
                        startActivity(Intent5);
                        cameraProvider.unbindAll();
                        finish();
                        break;

                    default:
                        System.out.println(R.string.no_face_detected);
                        break;
                }



            } else {
                detectionTextView.setText(R.string.no_face_detected);

            }
        }
        graphicOverlay.draw(boundingBox, scaleX, scaleY, "");
    }


    @SuppressWarnings("deprecation")
    private void loadModel() {
        try {
            //model name
            String modelFile = "mobile_face_net.tflite";
            tfLite = new Interpreter(loadModelFile(FaceRecognition.this, modelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private String SaveInternal(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "selfie.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void LoadInternal(String path) {

        try {
            File f = new File(path, "selfie.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            //create this imageview on the xml before uncommenting
            // ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //  img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
