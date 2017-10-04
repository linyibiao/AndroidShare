package com.lyb.besttimer.pluginwidget.view.refreshlayout;

/**
 * 拖拽距离的帮助类
 * Created by besttimer on 2017/10/4.
 */

public class DragDistanceHelper {

    /**
     * h=-k*k/(h*factor+k)+k)
     * x=-k*k/(y-k)-k
     */
    public double getValueX(double h, double factor, double y) {
        double k = h * factor / (factor - 1);
        return getFormulaX(k, y);
    }

    /**
     * h=-k*k/(h*factor+k)+k)
     * y=-k*k/(x+k)+k
     */
    public double getValueY(double h, double factor, double x) {
        double k = h * factor / (factor - 1);
        return getFormulaY(k, x);
    }

    /**
     * x=-k*k/(y-k)-k 关于y轴对称
     */
    private double getFormulaX(double k, double y) {
        if (y >= 0) {
            return k * y / (k - y);
        } else {
            return k * y / (k + y);
        }
    }

    /**
     * y=-k*k/(x+k)+k 关于y轴对称
     */
    private double getFormulaY(double k, double x) {
        if (x >= 0) {
            return k * x / (k + x);
        } else {
            return k * x / (k - x);
        }
    }

}
