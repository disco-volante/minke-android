package za.ac.sun.cs.hons.minke.gui.graph;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class TimelineChart {

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public GraphicalView execute(final Context context,
			ArrayList<BranchProduct> bps) {
		PointStyle[] values = PointStyle.values();
		PointStyle[] styles = new PointStyle[bps.size()];
		if (bps.size() > values.length) {
			System.arraycopy(values, 0, styles, 0, values.length);
			System.arraycopy(values, 0, styles, values.length, styles.length
					- values.length);
		} else {
			System.arraycopy(values, 0, styles, 0, styles.length);
		}

		final String[] titles = new String[bps.size()];
		List<double[]> prices = new ArrayList<double[]>();
		List<Date[]> dates = new ArrayList<Date[]>();
		Random color = new Random();
		int[] colors = new int[bps.size()];
		int i = 0;
		int maxPrice = 0, minPrice = Integer.MAX_VALUE;
		Date maxDate = null, minDate = null;
		for (BranchProduct bp : bps) {
			colors[i] = Color.argb(255, color.nextInt(256), color.nextInt(256),
					color.nextInt(256));
			titles[i++] = bp.getProduct().toString();
			ArrayList<DatePrice> hist = EntityUtils.getDatePrices(bp.getId());
			double[] p = new double[hist.size()];
			Date[] d = new Date[hist.size()];
			int j = 0;
			for (DatePrice ie : hist) {
				DatePrice dp = (DatePrice) ie;
				p[j] = dp.getActualPrice();
				if (p[j] > maxPrice) {
					maxPrice = (int) Math.ceil(p[j]);
				}
				if (minPrice > p[j]) {
					minPrice = (int) Math.round(p[j]);
				}
				if (maxDate == null || dp.getDate().after(maxDate)) {
					maxDate = dp.getDate();
				}
				if (minDate == null || dp.getDate().before(minDate)) {
					minDate = dp.getDate();
				}
				d[j++] = dp.getDate();

			}
			prices.add(p);
			dates.add(d);

		}
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, context.getString(R.string.chart_title),
				context.getString(R.string.chart_xaxis),
				context.getString(R.string.chart_yaxis), minDate, maxDate,
				minPrice, maxPrice);
		renderer.setXLabels(5);
		renderer.setYLabels(5);
		int length = renderer.getSeriesRendererCount();
		for (int k = 0; k < length; k++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(k))
					.setFillPoints(true);
		}
		final GraphicalView view = ChartFactory.getTimeChartView(context,
				buildDataset(titles, dates, prices), renderer, "dd/MM/yyyy");
		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(100);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = view
						.getCurrentSeriesAndPoint();
				if (seriesSelection != null) {
					Date date = new Date();
					date.setTime((long) seriesSelection.getXValue());
					Toast msg = Toast.makeText(
							context,
							context.getString(R.string.product)
									+ ": "
									+ titles[seriesSelection.getSeriesIndex()]
									+ "\n"
									+ context.getString(R.string.date)
									+ ": "
									+ DateFormat.getDateInstance(
											DateFormat.MEDIUM).format(date)
									+ "\n" + context.getString(R.string.price)
									+ ": R " + seriesSelection.getValue(),
							Toast.LENGTH_LONG);
					msg.setGravity(Gravity.CENTER_VERTICAL,
							Gravity.CENTER_HORIZONTAL, 0);
					msg.show();
				}
			}
		});
		return view;
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<Date[]> dates, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = dates.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors
	 *            the series rendering colors
	 * @param styles
	 *            the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(30);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsTextSize(25);
		renderer.setLegendTextSize(35);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 50, 50, 60, 10 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * Sets a few of the series renderer settings.
	 * 
	 * @param renderer
	 *            the renderer to set the properties to
	 * @param title
	 *            the chart title
	 * @param xTitle
	 *            the title for the X axis
	 * @param yTitle
	 *            the title for the Y axis
	 * @param maxDate
	 * @param minDate
	 * @param minDate
	 *            the minimum value on the X axis
	 * @param maxDate
	 *            the maximum value on the X axis
	 * @param yMin
	 *            the minimum value on the Y axis
	 * @param yMax
	 *            the maximum value on the Y axis
	 * @param axesColor
	 *            the axes color
	 * @param labelsColor
	 *            the labels color
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, Date xMin, Date xMax,
			double yMin, double yMax) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin.getTime());
		renderer.setXAxisMax(xMax.getTime());
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setShowGridY(true);
		renderer.setShowGridX(true);
	}

}
