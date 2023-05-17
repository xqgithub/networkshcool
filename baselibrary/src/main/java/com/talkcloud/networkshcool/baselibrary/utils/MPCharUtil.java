package com.talkcloud.networkshcool.baselibrary.utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/5/20
 * description: mpchart工具
 */

public class MPCharUtil {

    /**
     * 初始化饼状图
     *
     * @param mPieChart
     */
    public static void initPieChar(PieChart mPieChart) {
        //饼状图
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
//        mPieChart.setCenterText(generateCenterSpannableText());
//        mChart.setDrawCenterText(true);//饼状图中间可以添加文字
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        mPieChart.setNoDataText("饼状图暂无数据");
        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);

        //设置隐藏饼状图上文字
        mPieChart.setDrawEntryLabels(true);
        //半径
        mPieChart.setHoleRadius(58f);
        //半透明圆
        mPieChart.setTransparentCircleRadius(61f);
        //mChart.setHoleRadius(0)  //实心圆
        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);
        //显示百分比
        mPieChart.setUsePercentValues(false);
        mPieChart.animateY(1400, Easing.EaseInOutQuad); //设置动画
/*        Legend mLegend = mPieChart.getLegend();  //设置比例图
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);  //左下边显示
        mLegend.setFormSize(20f);//比例块字体大小
        mLegend.setXEntrySpace(10f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(10f);
        //设置比例块换行...
        mLegend.setWordWrapEnabled(true);
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);*/
//      mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
//      mLegend.setEnabled(false);//设置禁用比例块

        //变化监听
//        mPieChart.setOnChartValueSelectedListener(this);
        //设置数据
        //模拟数据
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//        entries.add(new PieEntry(3, "优秀    3人"));
//        entries.add(new PieEntry(5, "良好    5人"));
//        entries.add(new PieEntry(3, "及格    3人"));
//        entries.add(new PieEntry(1, "不及格  1人"));
//        setPieDate(mPieChart,entries,mLegend);
    }

    /**
     * 设置饼状图数据
     *
     * @param entries
     * @return
     */
    public static void setPieDate(PieChart mPieChart, ArrayList<PieEntry> entries, List<Integer> colors) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(false);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
        mPieChart.animateXY(1000, 1000);
    }

    /**
     * 初始化柱状图
     *
     * @param mBarChart
     */
    public static void initBarChart(BarChart mBarChart) {
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);//设置柱状图上数据显示的位置
        mBarChart.setHighlightFullBarEnabled(false);
        //设置y轴显示动画
        mBarChart.animateY(1000);
        mBarChart.setNoDataText("柱状图暂无数据");
        // 改变y标签的位置
        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setValueFormatter((value, axis) -> String.valueOf((int) value));
        leftAxis.setAxisMinimum(0f);
        //不显示y轴线条
//        leftAxis.setDrawAxisLine(false);
        mBarChart.getAxisRight().setEnabled(false);
        XAxis xLabels = mBarChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置x轴显示坐标在对应柱状图下
        xLabels.setGranularity(1f);
        xLabels.setDrawGridLines(false);
        //设置x轴显示的名称的个数
        xLabels.setLabelCount(5);

        //设置缩放
        //设置是否可以触摸
        mBarChart.setTouchEnabled(true);
        //设置是否可以拖拽
        mBarChart.setDragEnabled(true);
        //设置是否可以缩放x和y
        mBarChart.setScaleYEnabled(false);
        //x轴缩放比例
//        mBarChart.setScaleX(1.5f);
        //设置双击屏幕放大
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setFitBars(true);//使两侧的柱图完全显示

        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(30f);
    }

    /**
     * 单个柱状图
     *
     * @param barChart
     * @param yAxisValue
     * @param xValues
     * @param title
     * @param barColor
     */
    public static void setBarCharData(BarChart barChart, List<Float> yAxisValue, List<String> xValues, String title, Integer barColor) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        boolean isAllZero = true; //判断雨量是否都是0，是的话 则 y坐标自定义 max 和min
        for (int i = 0, n = yAxisValue.size(); i < n; ++i) {
            if (yAxisValue.get(i) != 0) {
                isAllZero = false;
            }
            entries.add(new BarEntry(i, yAxisValue.get(i)));
        }
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rigthAxis = barChart.getAxisRight();
        if (isAllZero) {

            leftAxis.setAxisMaximum((float) 1.00);
            leftAxis.setAxisMinimum((float) 0.00);
            rigthAxis.setAxisMaximum((float) 1.00);
            rigthAxis.setAxisMinimum((float) 0.00);
            leftAxis.setXOffset(10);
            rigthAxis.setXOffset(10);
        } else {
            leftAxis.setAxisMinimum((float) 0.00);
            leftAxis.resetAxisMaximum();
            rigthAxis.setAxisMinimum((float) 0.00);
            rigthAxis.resetAxisMaximum();
            leftAxis.setXOffset(10);
            rigthAxis.setXOffset(10);

        }

        // 数据描述
        Description description = new Description();
        description.setText("时间");
        barChart.setDescription(description);

        BarDataSet set1;
      /*  if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {*/
        set1 = new BarDataSet(entries, title);
        if (barColor == null) {
            //   set1.setColor(ContextCompat.getColor(barChart.getContext(), R.color.blue_user));//设置set1的柱的颜色
        } else {
            set1.setColor(barColor);
        }
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(10);
   /*     data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> {
            return String.valueOf(value);
        });*/
        barChart.setData(data);
        //   }
        //图表上不显示值
        barChart.getBarData().setDrawValues(false);
        XAxis xLabels = barChart.getXAxis();
        //  xLabels.setAxisMinimum(0);

        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setLabelCount(4, true);
        // xLabels.setXOffset(-90);
        xLabels.setGranularity(1f);
        // xLabels(true);

        //修改x轴显示文字  monthList.get((int)value%monthList.size())

/*        xLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int pisotion = (int) value % xValues.size();
                return xValues.get(pisotion);
            }
        });*/


        //    barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.invalidate();
        barChart.animateY(1000);
    }

    /**
     * 一个柱显示多个数据
     *
     * @param mBarChart
     */
    private void initMoreBarChartDate(BarChart mBarChart) {
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("2018-09");
        monthList.add("2018-10");
        monthList.add("2018-11");
        monthList.add("2018-09");
        monthList.add("2018-10");
        monthList.add("2018-11");
        monthList.add("2018-09");
        monthList.add("2018-10");
        monthList.add("2018-11");
        XAxis xLabels = mBarChart.getXAxis();
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
/*        xLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return monthList.get((int) value % monthList.size());
            }
        });*/
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < 8 + 1; i++) {
            float mult = (50 + 1);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;
            yVals1.add(new BarEntry(i, new float[]{val1, val2, val3}));
        }

        BarDataSet set1;

        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "三年级一班期末考试");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"及格", "优秀", "不及格"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
         /*   data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    //value就是显示柱状图的position  从0开始
                    return String.valueOf((int) value);
                }
            });*/
            data.setValueTextColor(Color.WHITE);
            //设置柱状图宽度
            data.setBarWidth(0.3f);
            mBarChart.setData(data);
        }
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
        mBarChart.animateY(1000);
    }

    private int[] getColors() {
        int stacksize = 3;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }
        return colors;
    }


    /**
     * 初始化折线图
     *
     * @param mLineChart
     */
    public static void initLineChart(LineChart mLineChart) {
        mLineChart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        mLineChart.setNoDataText("暂无数据");
        mLineChart.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mLineChart.setPinchZoom(false);
        //后台绘制
        mLineChart.setDrawGridBackground(false);
        //设置描述文本
        mLineChart.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChart.setTouchEnabled(true);
        //设置缩放
        mLineChart.setDragEnabled(true);
        //设置推动
        mLineChart.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChart.setPinchZoom(true);

        //x轴
        LimitLine llXAxis = new LimitLine(10f, "标记");
        //设置线宽
        llXAxis.setLineWidth(4f);
        //
//        llXAxis.enableDashedLine(10f, 10f, 0f);
        //设置
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        //设置x轴的最大值
//        xAxis.setAxisMaximum(100f);
//        //设置x轴的最小值
//        xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mLineChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        leftAxis.setAxisMaximum(200f);
        //y轴最小
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);

        //默认动画
        mLineChart.animateY(2500);
        //刷新
        //mChart.invalidate();

        // 得到这个文字
        Legend l = mLineChart.getLegend();

        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(30f);

        //这里我模拟一些数据
        ArrayList<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(5, 50));
        values.add(new Entry(10, 66));
        values.add(new Entry(15, 120));
        values.add(new Entry(20, 30));
        values.add(new Entry(35, 10));
        values.add(new Entry(40, 110));
        values.add(new Entry(45, 30));
        values.add(new Entry(50, 160));
        values.add(new Entry(100, 30));

        //设置数据
//        setData(values,mLineChart);
    }

    public static void setLineChartData(List<Float> yAxisValue, List<String> xValues, List<Float> yAxisValueTwo, LineChart mLineChar) {
        XAxis xLabels = mLineChar.getXAxis();
        // 设置x轴数据的位置
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        // xLabels.setValueFormatter((value, axis) -> String.valueOf(xValues.get((int) value % xValues.size())));
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValue.size(); i++) {
            values.add(new Entry(i, yAxisValue.get(i)));
        }
        Float max = Collections.max(yAxisValue);
        Float min = Collections.min(yAxisValue);
        Float len = (max - min) / 8;
        max = max + len;
        min = min - len;
        Float max2 = Collections.max(yAxisValueTwo);
        Float min2 = Collections.min(yAxisValueTwo);
        Float len2 = (max2 - min2) / 8;
        max2 = max2 + len2;
        min2 = min2 - len2;

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < yAxisValueTwo.size(); i++) {
            yVals2.add(new Entry(i, yAxisValueTwo.get(i)));
        }
        YAxis leftAxis = mLineChar.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        leftAxis.setAxisMaximum(max);
        //y轴最小
        leftAxis.setAxisMinimum(min);
        YAxis rightAxis = mLineChar.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(max2);
        rightAxis.setAxisMinimum(min2);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        LineDataSet set1, set2;

        if (mLineChar.getData() != null && mLineChar.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChar.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mLineChar.getData().getDataSetByIndex(1);
            set1.setValues(values);
            set2.setValues(yVals2);
            mLineChar.getData().notifyDataChanged();
            mLineChar.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, "水位");

            // 在这里设置虚线
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setDrawIcons(false);

//            if (Utils.getSDKInt() >= 18) {
//                // 填充背景只支持18以上
//                //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
//                //set1.setFillDrawable(drawable);
//                Drawable drawable = ContextCompat.getDrawable(com.tepia.base.utils.Utils.getContext(), R.drawable.fade_blue);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }
//            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//            //添加数据集
//            dataSets.add(set1);
            set2 = new LineDataSet(yVals2, "库容");

            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(ColorTemplate.getHoloBlue());
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(ColorTemplate.getHoloBlue());
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawCircleHole(false);
            set2.setDrawIcons(false);
            //创建一个数据集的数据对象
            LineData data = new LineData(set1, set2);

            //设置数据
            mLineChar.setData(data);
        }
//        List<ILineDataSet> setsHorizontalCubic = mLineChar.getData().getDataSets();
//        if (setsHorizontalCubic!=null&&setsHorizontalCubic.size()>0){
//            for (ILineDataSet iSet : setsHorizontalCubic) {
//                LineDataSet set = (LineDataSet) iSet;
//                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            }
//        }
        mLineChar.invalidate();
        mLineChar.animateY(2500);
    }


    public static void initLineChartTwo(LineChart lineChart) {

        // no description text
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("图表暂无数据");
        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        lineChart.setMaxVisibleValueCount(40);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
// 向左偏移15dp，抵消y轴向右偏移的30dp
        lineChart.setExtraLeftOffset(-15);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // set an alternative background color
//        lineChart.setBackgroundColor(Color.LTGRAY);

        lineChart.animateY(1000);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(3);
        xAxis.setXOffset(15);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        // 设置y轴数据偏移量
        leftAxis.setXOffset(15);
        // 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    public static void setLineData(List<Float> yAxisValue, List<String> xValues, List<Float> yAxisValueTwo, LineChart lineChart, String title1, String title2) {
        XAxis xLabels = lineChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setAxisMinimum(0);
        xLabels.setLabelCount(4, true);
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        //    xLabels.setValueFormatter((value, axis) -> String.valueOf(xValues.get((int) value % xValues.size())).substring(5, 16));
        Float max = Collections.max(yAxisValue);
        Float min = Collections.min(yAxisValue);
        float v = max - min;
        if (v < 1) {
            max = max + 1;
            min = min - 1;
        } else {
            Float len = (max - min) / 8;
            max = max + len;
            min = min - len * 3;
        }
        YAxis leftAxis = lineChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        leftAxis.setAxisMaximum(max);
        //y轴最小
        leftAxis.setAxisMinimum(min);
        leftAxis.setTextColor(Color.parseColor("#376fff"));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yAxisValue.size(); i++) {
            yVals1.add(new Entry(i, yAxisValue.get(i)));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        boolean isShowYTwo = true;
        for (int i = 0; i < yAxisValueTwo.size(); i++) {
            Float aFloat = yAxisValueTwo.get(i);
           /* if (i == 0) {
                if (aFloat == 0) {
                    isShowYTwo = false;
                }
            }*/
            if (aFloat == 0 && i != 0) {
                Float aFloat1 = null;
                for (int i1 = i; i1 >= 0; i1--) {
                    aFloat1 = yAxisValueTwo.get(i1);
                    if (aFloat1 != 0) {
                        break;
                    }
                }
                aFloat = aFloat1;
            }
            yVals2.add(new Entry(i, aFloat));
            yAxisValueTwo.set(i, aFloat);
//            if(i == 10) {
//                yVals2.add(new Entry(i, val + 50));
//            }
        }
        Float max2 = Collections.max(yAxisValueTwo);
        Float min2 = Collections.min(yAxisValueTwo);
        float v1 = max2 - min2;
        if (v1 < 1) {
            max2 = max2 + 1;
            min2 = min2 - 1;
        } else {
            Float len2 = (max2 - min2) / 8;
            max2 = max2 + len2;
            min2 = min2 - len2 * 3;
        }
        LineDataSet set1, set2;

//        if (lineChart.getData() != null &&
//                lineChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            if (isShowYTwo){
//                set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
//                set2.setValues(yVals2);
//            }
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        } else {
//
//        }
        // create a dataset and give it a type
        set1 = new LineDataSet(yVals1, title1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.parseColor("#376fff"));
        set1.setCircleColor(Color.parseColor("#376fff"));
        set1.setLineWidth(2f);
        set1.setDrawCircles(false);
//            set1.setCircleRadius(2f);
//            set1.setFillAlpha(65);
//            set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.parseColor("#376fff"));
        set1.setDrawCircleHole(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);
        // create a dataset and give it a type
        LineData data;
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setAxisMaximum(max2);
        rightAxis.setAxisMinimum(min2);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.rgb(10, 184, 180));
        if (isShowYTwo) {
            set2 = new LineDataSet(yVals2, title2);
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.rgb(10, 184, 180));
            set2.setCircleColor(Color.rgb(10, 184, 180));
            set2.setLineWidth(2f);
            set2.setDrawCircles(false);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(10, 184, 180));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            // create a data object with the datasets
            data = new LineData(set1, set2);
        } else {
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawLabels(false);
            data = new LineData(set1);
        }
//            data.setValueTextColor(Color.BLACK);
//            data.setValueTextSize(9f);
        //曲线图上不显示值
        data.setDrawValues(false);

        // 数据描述
        Description description = new Description();
        description.setText("时间");
        // description.setTextColor(ConfigConsts.colortext);
        lineChart.setDescription(description);

        // set data
        lineChart.setData(data);
        List<ILineDataSet> setsHorizontalCubic = lineChart.getData().getDataSets();
        if (setsHorizontalCubic != null && setsHorizontalCubic.size() > 0) {
            for (ILineDataSet iSet : setsHorizontalCubic) {
                LineDataSet set = (LineDataSet) iSet;
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            }
        }
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);

        lineChart.invalidate();
        lineChart.notifyDataSetChanged();
    }

    public static void setLineData(List<Float> yAxisValue, List<String> xValues, List<Float> yAxisValueTwo, List<Float> yAxisValueThree,
                                   LineChart lineChart, String title1, String title2, String title3) {
        XAxis xLabels = lineChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setAxisMinimum(0);
        xLabels.setLabelCount(4, true);
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        //  xLabels.setValueFormatter((value, axis) -> String.valueOf(xValues.get((int) value % xValues.size())).substring(5, 16));
        Float max = Collections.max(yAxisValue);
        Float min = Collections.min(yAxisValue);
        float v = max - min;
        if (v < 1) {
            max = max + 1;
            min = min - 1;
        } else {
            Float len = (max - min) / 8;
            max = max + len;
            min = min - len * 3;
        }
        YAxis rightAxis = lineChart.getAxisRight();
        //重置所有限制线,以避免重叠线
        rightAxis.removeAllLimitLines();
        //y轴最大
        rightAxis.setAxisMaximum(max);
        //y轴最小
        rightAxis.setAxisMinimum(min);
        rightAxis.setTextColor(Color.parseColor("#92d392"));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yAxisValue.size(); i++) {
            yVals1.add(new Entry(i, yAxisValue.get(i)));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValueTwo.size(); i++) {
            Float aFloat = yAxisValueTwo.get(i);

            if (aFloat == 0 && i != 0) {
                Float aFloat1 = null;
                for (int i1 = i; i1 >= 0; i1--) {
                    aFloat1 = yAxisValueTwo.get(i1);
                    if (aFloat1 != 0) {
                        break;
                    }
                }
                aFloat = aFloat1;
            }
            yVals2.add(new Entry(i, aFloat));
            yAxisValueTwo.set(i, aFloat);

        }
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValueThree.size(); i++) {
            Float aFloat = yAxisValueThree.get(i);

            if (aFloat == 0 && i != 0) {
                Float aFloat1 = null;
                for (int i1 = i; i1 >= 0; i1--) {
                    aFloat1 = yAxisValueThree.get(i1);
                    if (aFloat1 != 0) {
                        break;
                    }
                }
                aFloat = aFloat1;
            }
            yVals3.add(new Entry(i, aFloat));
            yAxisValueThree.set(i, aFloat);

        }
        Float max2 = Collections.max(yAxisValueTwo);
        Float min2 = Collections.min(yAxisValueTwo);

        Float max3 = Collections.max(yAxisValueThree);
        Float min3 = Collections.min(yAxisValueThree);
        if (max2 < max3) {  //取 23的最大值
            max2 = max3;
        }
        if (min3 < min2) {  //取23的最小值
            min2 = min3;
        }

        float v1 = max2 - min2;
        if (v1 < 0.0001f) {
            max2 = max2 + 0.0001f;
            min2 = min2 - 0.0001f;
        } else {
            max2 = max2 + 1;
            min2 = min2 - 1;
        }
        LineDataSet set1, set2, set3;


        set1 = new LineDataSet(yVals1, title1);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set1.setColor(Color.parseColor("#92d392"));
        set1.setCircleColor(Color.parseColor("#92d392"));
        set1.setLineWidth(2f);
        set1.setDrawCircles(false);

        set1.setHighLightColor(Color.parseColor("#92d392"));
        set1.setDrawCircleHole(false);

        LineData data;
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(max2);
        leftAxis.setAxisMinimum(min2);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setGranularityEnabled(false);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawLabels(true);
        //leftAxis.setTextColor(Color.rgb(10, 184, 180));

        set2 = new LineDataSet(yVals2, title2);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.parseColor("#4995f0"));
        set2.setCircleColor(Color.parseColor("#4995f0"));
        set2.setLineWidth(2f);
        set2.setDrawCircles(false);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(10, 184, 180));

        set3 = new LineDataSet(yVals3, title3);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setColor(Color.parseColor("#fd8e19"));
        set3.setCircleColor(Color.parseColor("#fd8e19"));
        set3.setLineWidth(2f);
        set3.setDrawCircles(false);
        set3.setDrawCircleHole(false);
        set3.setHighLightColor(Color.rgb(10, 184, 180));

        data = new LineData(set1, set2, set3);


        //曲线图上不显示值
        data.setDrawValues(false);

        // 数据描述
        Description description = new Description();
        description.setText("时间");
        // description.setTextColor(ConfigConsts.colortext);
        lineChart.setDescription(description);

        // set data
        lineChart.setData(data);
        List<ILineDataSet> setsHorizontalCubic = lineChart.getData().getDataSets();
        if (setsHorizontalCubic != null && setsHorizontalCubic.size() > 0) {
            for (ILineDataSet iSet : setsHorizontalCubic) {
                LineDataSet set = (LineDataSet) iSet;
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            }
        }
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);

        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        lineChart.invalidate();
        lineChart.notifyDataSetChanged();
    }

    public static void setLineData(List<Float> yAxisValue, List<String> xValues, LineChart lineChart, String title1) {
        XAxis xLabels = lineChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setLabelCount(3);
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        //  xLabels.setValueFormatter((value, axis) -> String.valueOf(xValues.get((int) value % xValues.size())));
        Float max = Collections.max(yAxisValue);
        Float min = Collections.min(yAxisValue);
        float v = max - min;
        if (v < 1) {
            max = max + 1;
            min = min - 1;
        } else {
            Float len = (max - min) / 8;
            max = max + len;
            min = min - len * 3;
        }
        YAxis leftAxis = lineChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        leftAxis.setAxisMaximum(max);
        //y轴最小
        leftAxis.setAxisMinimum(min);
        leftAxis.setTextColor(Color.parseColor("#376fff"));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yAxisValue.size(); i++) {
            yVals1.add(new Entry(i, yAxisValue.get(i)));
        }

        LineDataSet set1, set2;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals1, title1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.parseColor("#376fff"));
        set1.setCircleColor(Color.parseColor("#376fff"));
        set1.setLineWidth(2f);
        set1.setDrawCircles(false);
//            set1.setCircleRadius(2f);
//            set1.setFillAlpha(65);
//            set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.parseColor("#376fff"));
        set1.setDrawCircleHole(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);
        // create a dataset and give it a type
        LineData data;
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.rgb(10, 184, 180));
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        data = new LineData(set1);
//            data.setValueTextColor(Color.BLACK);
//            data.setValueTextSize(9f);
        //曲线图上不显示值
        data.setDrawValues(false);

        // set data
        lineChart.setData(data);
        List<ILineDataSet> setsHorizontalCubic = lineChart.getData().getDataSets();
        if (setsHorizontalCubic != null && setsHorizontalCubic.size() > 0) {
            for (ILineDataSet iSet : setsHorizontalCubic) {
                LineDataSet set = (LineDataSet) iSet;
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            }
        }
        lineChart.invalidate();
        lineChart.notifyDataSetChanged();
    }

    public static void setLineData(List<String> xValues, List<Float> yAxisValue, List<Float> yAxisValueTwo,
                                   List<Float> yAxisValueThree, List<Float> yAxisValueFour, List<Float> yAxisValueFive, LineChart lineChart,
                                   String title1, String title2, String title3, String title4, String title5) {

        // 数据描述
        Description description = new Description();
        description.setText("时间");
        // description.setTextColor(ConfigConsts.colortext);
        lineChart.setDescription(description);


        XAxis xLabels = lineChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setLabelCount(4, true);
        //修改x轴显示文字  monthList.get((int)value%monthList.size())
        //  xLabels.setValueFormatter((value, axis) -> String.valueOf(xValues.get((int) value % xValues.size())));
        //    xLabels.setYOffset(-30);
        //寻找5个纵坐标中的最大最小值
        List<Float> tempList = new ArrayList<>();
        tempList.add(Collections.min(yAxisValue));
        tempList.add(Collections.min(yAxisValueTwo));
        tempList.add(Collections.min(yAxisValueThree));
        tempList.add(Collections.min(yAxisValueFour));
        tempList.add(Collections.min(yAxisValueFive));
        tempList.add(Collections.max(yAxisValue));
        tempList.add(Collections.max(yAxisValueTwo));
        tempList.add(Collections.max(yAxisValueThree));
        tempList.add(Collections.max(yAxisValueFour));
        tempList.add(Collections.max(yAxisValueFive));


        Float max = Collections.max(tempList);
        Float min = Collections.min(tempList);

        float v = max - min;
        if (v < 1) {
            max = max + 1;
            min = min - 1;
        } else {
            Float len = (max - min) / 8;
            max = max + len;
            min = min - len * 3;
        }
        YAxis leftAxis = lineChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        leftAxis.setAxisMaximum(max);
        //y轴最小
        leftAxis.setAxisMinimum(min);
        leftAxis.setTextColor(Color.parseColor("#376fff"));
        //

        leftAxis.setXOffset(15);
        lineChart.getAxisRight().setXOffset(15);
        lineChart.getAxisRight().setAxisMaximum(max);
        //y轴最小
        lineChart.getAxisRight().setAxisMinimum(min);
        lineChart.getAxisRight().setTextColor(Color.parseColor("#376fff"));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yAxisValue.size(); i++) {
            yVals1.add(new Entry(i, yAxisValue.get(i)));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValue.size(); i++) {
            Float aFloat = yAxisValueTwo.get(i);

            if (aFloat == 0 && i != 0) {
                Float aFloat1 = null;
                for (int i1 = i; i1 >= 0; i1--) {
                    aFloat1 = yAxisValueTwo.get(i1);
                    if (aFloat1 != 0) {
                        break;
                    }
                }
                aFloat = aFloat1;
            }
            yVals2.add(new Entry(i, aFloat));
            yAxisValueTwo.set(i, aFloat);
//            if(i == 10) {
//                yVals2.add(new Entry(i, val + 50));
//            }
        }

        LineDataSet set1, set2, set3, set4, set5;


        set1 = new LineDataSet(yVals1, title1);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.parseColor("#F29F3A"));
        set1.setCircleColor(Color.parseColor("#F29F3A"));
        set1.setLineWidth(2f);
        set1.setDrawCircles(false);
        set1.setHighLightColor(Color.parseColor("#F29F3A"));
        set1.setDrawCircleHole(false);

        LineData data;

        set2 = new LineDataSet(yVals2, title2);
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.parseColor("#B88DE9"));
        set2.setCircleColor(Color.parseColor("#B88DE9"));
        set2.setLineWidth(2f);
        set2.setDrawCircles(false);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.parseColor("#B88DE9"));

        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValueThree.size(); i++) {
            yVals3.add(new Entry(i, yAxisValueThree.get(i)));
        }
        set3 = new LineDataSet(yVals3, title3);
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(Color.parseColor("#78C878"));
        set3.setCircleColor(Color.parseColor("#78C878"));
        set3.setLineWidth(2f);
        set3.setDrawCircles(false);
        set3.setDrawCircleHole(false);
        set3.setHighLightColor(Color.parseColor("#78C878"));

        ArrayList<Entry> yVals4 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValueFour.size(); i++) {
            yVals4.add(new Entry(i, yAxisValueFour.get(i)));
        }
        set4 = new LineDataSet(yVals4, title4);
        set4.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set4.setColor(Color.parseColor("#2CBAEE"));
        set4.setCircleColor(Color.parseColor("#2CBAEE"));
        set4.setLineWidth(2f);
        set4.setDrawCircles(false);
        set4.setDrawCircleHole(false);
        set4.setHighLightColor(Color.parseColor("#2CBAEE"));

        ArrayList<Entry> yVals5 = new ArrayList<Entry>();
        for (int i = 0; i < yAxisValueFive.size(); i++) {
            yVals5.add(new Entry(i, yAxisValueFive.get(i)));
        }
        set5 = new LineDataSet(yVals5, title5);
        set5.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set5.setColor(Color.parseColor("#FF9999"));
        set5.setCircleColor(Color.parseColor("#FF9999"));
        set5.setLineWidth(2f);
        set5.setDrawCircles(false);
        set5.setDrawCircleHole(false);
        set5.setHighLightColor(Color.parseColor("#FF9999"));
        //set2.setFillFormatter(new MyFillFormatter(900f));

        // create a data object with the datasets
        data = new LineData(set1, set2, set3, set4, set5);


//            data.setValueTextColor(Color.BLACK);
//            data.setValueTextSize(9f);
        //曲线图上不显示值
        data.setDrawValues(false);

        // set data
        lineChart.setData(data);
       /* List<ILineDataSet> setsHorizontalCubic = lineChart.getData().getDataSets();
        if (setsHorizontalCubic != null && setsHorizontalCubic.size() > 0) {
            for (ILineDataSet iSet : setsHorizontalCubic) {
                LineDataSet set = (LineDataSet) iSet;
                set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            }
        }*/
       /* Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);*/

        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);


       /* lineChart.invalidate();
        lineChart.setEnabled(true);
        lineChart.notifyDataSetChanged();*/

        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        // l.setDrawInside(true);

        lineChart.invalidate();
        lineChart.setEnabled(true);
        lineChart.notifyDataSetChanged();
       /* lineChart.invalidate();
        lineChart.setEnabled(true);
        lineChart.notifyDataSetChanged();*/
    }


    private static String getColor(int i) {
        String[] colors = {"#fca144", "#b88de9", "#2c9bee", "#78c878", "#f54142", "#F3D760", "#232A39", "#08af17"};
        if (i > 8) {
            i = i % 8;
        }
        return colors[i];
    }
}
