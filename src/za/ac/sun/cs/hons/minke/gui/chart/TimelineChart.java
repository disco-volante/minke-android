package za.ac.sun.cs.hons.minke.gui.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import za.ac.sun.cs.hons.minke.utils.constants.TIME;
import android.app.AlertDialog;
import android.util.Log;
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
	List<Integer> colours = new ArrayList<Integer>(Arrays.asList( new Integer[] { 0xFFFFB300, 0xFF803E75, 0xFFFF6800,
			0xFFA6BDD7, 0xFFC10020, 0xFFCEA262, 0xFF817066, 0xFF007D34,
			0xFFF6768E, 0xFF00538A, 0xFFFF7A5C, 0xFF53377A, 0xFFFF8E00,
			0xFFB32851, 0xFFF4C800, 0xFF7F180D, 0xFF93AA00, 0xFF593315,
			0xFFF13A13, 0xFF232C16 }));
	private HashSet<Integer> usedColours = new HashSet<Integer>();
	private HashSet<String> added, removed;
	private int numItems;
	private double maxY, minY, maxX, minX;

	public TimelineChart(ChartActivity _activity, int _numItems) {
		activity = _activity;
		if(_numItems > colours.size()){
			_numItems = colours.size();
		}
		numItems = _numItems;
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
		List<double[]> prices = new ArrayList<double[]>();
		List<Date[]> dates = new ArrayList<Date[]>();
		maxY = 0;
		minY = Double.MAX_VALUE;
		maxX = 0;
		minX = Double.MAX_VALUE;
		Set<String> titleSet = new HashSet<String>();
		for (BranchProduct bp : bps) {
			String title = bp.getProduct().toString() + " ("
					+ bp.getBranch().toString() + ")";
			if (!titleSet.add(title)) {
				continue;
			}
			ArrayList<DatePrice> hist = EntityUtils.getDatePrices(activity,
					bp.getId());
			double[] p = new double[hist.size()];
			Date[] d = new Date[hist.size()];
			getData(hist, p, d, titleSet.size() <= numItems);
			prices.add(p);
			dates.add(d);

		}
		titles = new String[titleSet.size()];
		titles = titleSet.toArray(titles);
		maxY += 1;
		if (minY > 0) {
			minY -= 1;
		}
		minX -= TIME.DAY;
		maxX += TIME.DAY;
		renderer = buildRenderer(titleSet.size(), styles);
		setChartSettings(renderer, activity.getString(R.string.chart_title),
				activity.getString(R.string.chart_xaxis),
				activity.getString(R.string.chart_yaxis));
		renderer.setXLabels(6);
		renderer.setYLabels(5);
		int length = renderer.getSeriesRendererCount();
		for (int k = 0; k < length; k++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(k))
					.setFillPoints(true);
		}
		dataset = buildDataset(titles, dates, prices);
		loaded = true;
	}



	private void getData(ArrayList<DatePrice> hist, double[] p, Date[] d,
			boolean setExtremes) {
		int j = 0;
		for (DatePrice dp : hist) {
			p[j] = dp.getActualPrice();
			if (setExtremes) {
				if (p[j] > maxY) {
					maxY = (int) Math.ceil(p[j]);
				}
				if (minY > p[j]) {
					minY = (int) Math.floor(p[j]);
				}
				if (dp.getDate().getTime() > maxX) {
					maxX = dp.getDate().getTime();
				}
				if (dp.getDate().getTime() < minX) {
					minX = dp.getDate().getTime();
				}
			}
			d[j++] = dp.getDate();
			if (DEBUG.ON) {
				Log.v(TAGS.CHART, "date " + d[j - 1]);
			}
		}
	}

	public GraphicalView getChart() {
		if (!loaded) {
			return null;
		}
		final GraphicalView view = ChartFactory.getTimeChartView(activity,
				dataset, renderer, "dd-MMM-yyyy");
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
					AlertDialog.Builder dialog = DialogUtils.getGraphDialog(
							activity, titles[seriesSelection.getSeriesIndex()],
							date, seriesSelection.getValue());
					if (dialog != null) {
						dialog.show();
					}
				}
			}
		});
		return view;
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<Date[]> dates, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		added = new HashSet<String>();
		removed = new HashSet<String>();
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = dates.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			rendererMap.put(titles[i], renderer.getSeriesRendererAt(i));
			seriesMap.put(titles[i], series);
			if (i < numItems) {
				added.add(titles[i]);
				usedColours.add(colours.get(0));
				rendererMap.get(titles[i]).setColor(colours.remove(0));
				dataset.addSeries(series);
			} else {
				removed.add(titles[i]);
			}

		}
		for (String r : removed) {
			renderer.removeSeriesRenderer(rendererMap.get(r));
		}
		return dataset;
	}

	private void removeSeries(String title) {
		added.remove(title);
		removed.add(title);
		dataset.removeSeries(seriesMap.get(title));
		renderer.removeSeriesRenderer(rendererMap.get(title));
		usedColours.remove(rendererMap.get(title).getColor());
		colours.add(rendererMap.get(title).getColor());

	}

	private void addSeries(String title) {
		if(colours.size() == 0){
			Toast.makeText(activity, activity.getString(R.string.chart_full), Toast.LENGTH_SHORT).show();
			return;
		}
		added.add(title);
		removed.remove(title);
		TimeSeries series = seriesMap.get(title);
		dataset.addSeries(series);
		usedColours.add(colours.get(0));
		rendererMap.get(title).setColor(colours.remove(0));
		renderer.addSeriesRenderer(rendererMap.get(title));
		getExtremes(series);

	}

	private void getExtremes(TimeSeries series) {
		if (series.getMaxX() > maxX) {
			maxX = series.getMaxX();
		}
		if (series.getMinX() < minX) {
			minX = series.getMinX();
		}
		if (series.getMaxY() > maxY) {
			maxY = series.getMaxY();
		}
		if (series.getMinY() < minY) {
			minY = series.getMinY();
		}
	}

	public void addSeries(Set<String> changed) {
		for (String s : changed) {
			addSeries(s);
		}
		showExtremes();
	}

	public void removeSeries(Set<String> changed) {
		for (String s : changed) {
			removeSeries(s);
		}
		for (String s : added) {
			getExtremes(seriesMap.get(s));
		}
		showExtremes();

	}

	private void showExtremes() {
		renderer.setXAxisMin(minX);
		renderer.setXAxisMax(maxX);
		renderer.setYAxisMin(minY);
		renderer.setYAxisMax(maxY);
		renderer.setInitialRange(new double[] { minX, maxX, minY, maxY });
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
	protected XYMultipleSeriesRenderer buildRenderer(int size, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(20);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setPointSize(10f);
		renderer.setFitLegend(true);
		renderer.setMargins(new int[] { 50, 50, 60, 10 });
		int stylePos = 0;
		for (int i = 0; i < size; i ++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setLineWidth(3f);
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
			String title, String xTitle, String yTitle) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		showExtremes();
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

}
