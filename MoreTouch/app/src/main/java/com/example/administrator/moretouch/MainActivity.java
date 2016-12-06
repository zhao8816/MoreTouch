package com.example.administrator.moretouch;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 本工程演示两点触控。
 */
public class MainActivity extends AppCompatActivity {

    private ImageView image;
    //矩阵
    private Matrix matrix;
    private Matrix newMatrix;
    //存放坐标
    private PointF startPoint;
    private PointF middlePoint;
    //两个触控点的间距
    private double startSpacing;
    private double endSpacing;
    //记录屏幕触控点的数量
    private int NUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        //实例化两个矩阵
        matrix = new Matrix();
        newMatrix = new Matrix();
        //实例化两个PointF类的对象，存放某一点的坐标
        startPoint = new PointF();
        middlePoint = new PointF();
        //图片设置触摸事件
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN://单指按下
                        //设置图片矩阵
                        matrix.set(image.getImageMatrix());
                        //把这个矩阵设置给一个移动矩阵
                        newMatrix.set(matrix);
                        //存放当前点的坐标
                        startPoint.set(motionEvent.getX(), motionEvent.getY());
                        //记录触控点数量
                        NUM = 1;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN://多指按下
                        //获取两指按下时，两个触控点的距离
                        startSpacing=spacing(motionEvent);
                        //设置图片矩阵
                        matrix.set(image.getImageMatrix());
                        //把这个矩阵设置给一个移动矩阵
                        newMatrix.set(matrix);
                        //获取中心点位置
                        getMiddlePoint(middlePoint, motionEvent);
                        //记录触控点数量
                        NUM = 2;
                        break;
                    case MotionEvent.ACTION_UP://单指抬起
                        NUM = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_UP://多指抬起
                        NUM = 0;
                        break;
                    case MotionEvent.ACTION_MOVE://手指移动
                        if (NUM == 1) {//单指操作，图片位移
                            newMatrix.set(matrix);
                            newMatrix.postTranslate(motionEvent.getX()-startPoint.x,motionEvent.getY()-startPoint.y);
                        } else if (NUM == 2) {//多指操作，图片缩放
                            //获取两指滑动后，两个触控点的距离
                            endSpacing=spacing(motionEvent);
                            newMatrix.set(matrix);
                            //计算图片缩放比例
                            double scale = endSpacing/startSpacing;
                            //图片缩放，参数：x、y轴缩放比例，缩放中心点
                            newMatrix.postScale((float) scale,(float) scale,middlePoint.x,middlePoint.y);
                        }
                        break;
                }
                image.setImageMatrix(newMatrix);
                return true;
            }

            //计算移动距离
            private double spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return Math.sqrt(x * x + y * y);
            }
            //计算中点位置
            private void getMiddlePoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });
    }
}
