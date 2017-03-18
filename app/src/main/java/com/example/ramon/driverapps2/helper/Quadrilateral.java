package com.example.ramon.driverapps2.helper;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

/**
 * Created by ramon on 3/17/2017.
 */

public class Quadrilateral {
    public MatOfPoint contour;
    public Point[] points;

    public Quadrilateral(MatOfPoint contour, Point[] points) {
        this.contour = contour;
        this.points = points;
    }
}
