package com.example.chartviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.chartviewexample.ValueLabelAdapter.LabelOrientation;
import com.fima.chartview.ChartView;
import com.fima.chartview.LinearSeries;
import com.fima.chartview.LinearSeries.LinearPoint;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Find the chart view
		ChartView chartView = (ChartView) findViewById(R.id.chart_view);

		// Create the data points
		LinearSeries series = new LinearSeries();
		series.setLineColor(0xFF0099CC);
		series.setLineWidth(2);

		for (double i = 0d; i <= (2d * Math.PI); i += 0.1d) {
			series.addPoint(new LinearPoint(i, Math.sin(i)));
		}

		// Add chart view data
		chartView.addSeries(series);
		chartView.setLeftLabelAdapter(new ValueLabelAdapter(this, LabelOrientation.VERTICAL));
		chartView.setBottomLabelAdapter(new ValueLabelAdapter(this, LabelOrientation.HORIZONTAL));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
