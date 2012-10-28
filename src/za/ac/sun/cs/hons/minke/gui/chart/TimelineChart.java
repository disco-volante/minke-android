package za.ac.sun.cs.hons.minke.gui.chart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class TimelineChart {

	private XYMultipleSeriesDataset dataset;
	private ChartActivity activity;
	private boolean loaded;
	private XYMultipleSeriesRenderer renderer;
	private String[] titles;
	private HashMap<String, TimeSeries> seriesMap = new HashMap<String, TimeSeries>();
	private HashMap<String, SimpleSeriesRenderer> rendererMap = new HashMap<String, SimpleSeriesRenderer>();

	private HashSet<String> added, removed;

	public TimelineChart(ChartActivity _activity) {
		this.activity = _activity;
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public void buildData(ArrayList<BranchProduct> bps) {
		PointStyle[] styles = PointStyle.values();
		titles = new String[bps.size()];
		List<double[]> prices = new ArrayList<double[]>();
		List<Date[]> dates = new ArrayList<Date[]>();
		Random rCol = new Random();
		int[] colours = new int[bps.size()];
		int i = 0;
		int maxPrice = 0, minPrice = Integer.MAX_VALUE;
		Date maxDate = null, minDate = null;
		for (BranchProduct bp : bps) {
			int colour = 0x000000;
			while (!darkEnough(colour)) {
				colour = Color.argb(255, rCol.nextInt(256), rCol.nextInt(256),
						rCol.nextInt(256));
			}
			if (DEBUG.ON) {
				Log.v(TAGS.CHART, "colour " + colour);
			}
			colours[i] = colour;
			titles[i++] = bp.getProduct().toString() + " ("
					+ bp.getBranch().toString() + ")";
			ArrayList<DatePrice> hist = EntityUtils.getDatePrices(bp.getId());
			double[] p = new double[hist.size()];
			Date[] d = new Date[hist.size()];
			int j = 0;
			for (DatePrice ie : hist) {
				DatePrice dp = ie;
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
				if (DEBUG.ON) {
					Log.v(TAGS.CHART, "date " + d[j - 1]);
				}
			}
			prices.add(p);
			dates.add(d);

		}
		renderer = buildRenderer(colours, styles);
		setChartSettings(renderer, activity.getString(R.string.chart_title),
				activity.getString(R.string.chart_xaxis),
				activity.getString(R.string.chart_yaxis), minDate, maxDate,
				minPrice, maxPrice);
		renderer.setXLabels(5);
		renderer.setYLabels(5);
		int length = renderer.getSeriesRendererCount();
		for (int k = 0; k < length; k++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(k))
					.setFillPoints(true);
		}
		dataset = buildDataset(titles, dates, prices);
		loaded = true;
	}

	public GraphicalView getChart() {
		if (!loaded) {
			return null;
		}
		final GraphicalView view = ChartFactory.getTimeChartView(activity,
				dataset, renderer, "dd-MMM-yyyy");
		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(100);
		view.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				SeriesSelection seriesSelection = view
						.getCurrentSeriesAndPoint();
				if (seriesSelection != null) {
					if (added.contains(titles[seriesSelection.getSeriesIndex()])) {
						Date date = new Date();
						date.setTime((long) seriesSelection.getXValue());
						Toast msg = Toast.makeText(
								activity,
								activity.getString(R.string.product)
										+ ": "
										+ titles[seriesSelection
												.getSeriesIndex()]
										+ "\n"
										+ activity.getString(R.string.date)
										+ ": "
										+ DateFormat.getDateInstance(
												DateFormat.MEDIUM).format(date)
										+ "\n"
										+ activity.getString(R.string.price)
										+ ": R " + seriesSelection.getValue(),
								Toast.LENGTH_LONG);
						msg.setGravity(Gravity.CENTER_VERTICAL,
								Gravity.CENTER_HORIZONTAL, 0);
						msg.show();
						return true;
					}
				}
				return false;
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
			seriesMap.put(titles[i], series);
			rendererMap.put(titles[i], renderer.getSeriesRendererAt(i));

		}
		added = new HashSet<String>();
		removed = new HashSet<String>();
		added.addAll(Arrays.asList(titles));
		return dataset;
	}

	public void removeSeries(String title) {
		added.remove(title);
		removed.add(title);
		dataset.removeSeries(seriesMap.get(title));
		renderer.removeSeriesRenderer(rendererMap.get(title));
	}

	public void addSeries(String title) {
		added.add(title);
		removed.remove(title);
		dataset.addSeries(seriesMap.get(title));
		renderer.addSeriesRenderer(rendererMap.get(title));
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
	protected XYMultipleSeriesRenderer buildRenderer(int[] colours,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setPointSize(3f);
		renderer.setFitLegend(true);
		renderer.setMargins(new int[] { 50, 50, 60, 10 });
		int length = colours.length;
		int stylePos = 0;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colours[i]);
			r.setPointStyle(styles[stylePos++]);
			if (stylePos == styles.length) {
				stylePos = 0;
			}
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
	 * @param background
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
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(activity.getResources().getColor(
				android.R.color.white));
		renderer.setLabelsColor(activity.getResources().getColor(
				android.R.color.black));
		renderer.setXLabelsColor(activity.getResources().getColor(
				android.R.color.black));
		renderer.setYLabelsColor(0,
				activity.getResources().getColor(android.R.color.black));
		renderer.setAxesColor(activity.getResources().getColor(
				android.R.color.black));
		renderer.setGridColor(activity.getResources().getColor(
				android.R.color.black));
		renderer.setMarginsColor(activity.getResources().getColor(
				android.R.color.white));

	}

	public ChartActivity getGraphActivity() {
		return activity;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public HashSet<String> getAdded() {
		return added;
	}

	public HashSet<String> getRemoved() {
		return removed;
	}

	private static boolean darkEnough(int colour) {
		return Math.sqrt(Color.red(colour) * Color.red(colour) * .241
				+ Color.green(colour) * Color.green(colour) * .691
				+ Color.blue(colour) * Color.blue(colour) * .068) > 130;
	}

}
