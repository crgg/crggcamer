package com.example.ramon.driverapps2;

import android.graphics.Camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;
    private Mat mRgbaF;
    private Mat mRgbaT;




    Mat hierarchy;
    List<MatOfPoint> contours;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
//            super.onManagerConnected(status);
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
               {
                      mOpenCvCameraView.enableView();
                      mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                } break;
                default:{
                    super.onManagerConnected(status);
                } break;


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_gracias_DIOS);
        mOpenCvCameraView.SetCaptureFormat(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {


            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mLoaderCallback);

        } else {

            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
    }
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }



    @Override
    public void onCameraViewStarted(int width, int height)
    {



        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
        hierarchy = new Mat();
    }

    @Override
    public void onCameraViewStopped()
    {
        mRgba.release();

        hierarchy.release();
    }

    @Override
   public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        contours = new ArrayList<MatOfPoint>();
        hierarchy = new Mat();

        if (mRgba!=null)
        {
            mRgba = inputFrame.rgba();

            Mat mRgbaT = mRgba.t ();

            Core.transpose(mRgba, mRgbaT);

            Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);

            Core.flip(mRgbaF, mRgba, 1 );



            // procesar la fotografias

            Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_BGR2HSV);



            hierarchy = new Mat();

            Imgproc.Canny(mRgba, mRgbaF, 80, 100);

            Imgproc.findContours(mRgbaF, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,
                    new Point(0,0));

            hierarchy.release();

            Imgproc.drawContours(mRgba,contours, -1, new Scalar(255,255,0));

        }

        return mRgba;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
   }


    public void Find_Square(Mat image,ArrayList<MatOfPoint> squares ) {


    }


}


