package com.meuapp;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Arrays;
import java.util.Random;

public class SortingPerformanceTest extends ApplicationFrame {
    public SortingPerformanceTest(String title) {
        super(title);
    }

    public static void main(String[] args) {
        SortingPerformanceTest test = new SortingPerformanceTest("Sorting Algorithm Performance");
        test.runTests();
        test.pack();
        RefineryUtilities.centerFrameOnScreen(test);
        test.setVisible(true);
    }

    public void runTests() {
        int[] sizes = {1000, 5000, 10000, 20000, 30000};
        int repetitions = 3;
        XYSeriesCollection dataset = createDataset();

        JFreeChart chart = ChartFactory.createXYLineChart("Sorting Algorithms Performance",
                "Input Size", "Time (ms)", dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (SortAlgorithm algorithm : new SortAlgorithm[]{new BubbleSort(), new InsertionSort(), new MergeSort()}) {
            for (String inputType : new String[]{"Crescente", "Decrescente", "Aleatório"}) {
                XYSeries series = new XYSeries(algorithm.getClass().getSimpleName() + " - " + inputType);

                int[] sizes = {1000, 5000, 10000, 20000, 30000};
                int repetitions = 3;

                for (int i = 0; i < sizes.length; i++) {
                    long totalTime = 0;

                    for (int j = 0; j < repetitions; j++) {
                        int[] arr = generateTestData(sizes[i], inputType);
                        int[] arrCopy = Arrays.copyOf(arr, arr.length);

                        long startTime = System.nanoTime();
                        algorithm.sort(arrCopy);
                        long endTime = System.nanoTime();

                        totalTime += (endTime - startTime);

                        // Verificar se a ordenação está correta (opcional)
                        assert isSorted(arrCopy);
                    }

                    double averageTime = totalTime / (double) repetitions;
                    series.add(sizes[i], averageTime / 1e6);  // Convert to milliseconds
                }

                dataset.addSeries(series);
            }
        }

        return dataset;
    }

    private int[] generateTestData(int size, String inputType) {
        int[] arr = new int[size];
        Random random = new Random();

        if (inputType.equals("Crescente")) {
            for (int i = 0; i < size; i++) {
                arr[i] = i;
            }
        } else if (inputType.equals("Decrescente")) {
            for (int i = 0; i < size; i++) {
                arr[i] = size - i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(size);
            }
        }

        return arr;
    }

    private boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
