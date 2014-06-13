package com.mobimation.radiofyr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobimation.radiofyr.R;
import com.aprilbrother.aprilbrothersdk.Beacon;

public class BeaconAdapter2 extends ArrayAdapter<Beacon> {

	// private ArrayList<Beacon> beacons;
	private LayoutInflater layoutInflater;
    List<Beacon> beacons;
	
	public BeaconAdapter2(Context context,ArrayList<Beacon> beacons) {
		super(context,R.layout.device_item,beacons);
		this.beacons=beacons;
		layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void replaceWith(Collection<Beacon> newBeacons) {
	    this.beacons.clear();
	    this.beacons.addAll(newBeacons);
	    notifyDataSetChanged();
	  }
/*
	@Override
	  public int getCount() {
	    return beacons.size();
	  }

	  @Override
	  public Beacon getItem(int position) {
	    return beacons.get(position);
	  }

	  @Override
	  public long getItemId(int position) {
	    return position;
	  }
*/
	  @Override
	  public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view==null) {
		      view = layoutInflater.inflate(R.layout.device_item, null);
		      holder=new ViewHolder(view);
		      view.setTag(holder);
		}
		else
			holder=(ViewHolder) view.getTag();
		
	    // view = inflateIfRequired(view, position, parent);
	    bind(getItem(position), view);
	    return view;
	  }

	  private void bind(Beacon beacon, View view) {
	    ViewHolder holder = (ViewHolder) view.getTag();
		DecimalFormat formatter = new DecimalFormat("##.##");
		String mac=beacon.getMacAddress();
		// TODO getDistance() crashes
		// double dist=beacon.getDistance();
	    holder.macTextView.setText(String.format("MAC: %s", beacon.getMacAddress()));
	    holder.uuidTextView.setText(beacon.getProximityUUID());
	    holder.majorTextView.setText(""+beacon.getMajor());
	    holder.minorTextView.setText(""+beacon.getMinor());
	    holder.measuredPowerTextView.setText(""+beacon.getMeasuredPower());
	    holder.rssiTextView.setText(""+beacon.getRssi());
	  }
/*
	  private View inflateIfRequired(View view, int position, ViewGroup parent) {
	    if (view == null) {
	      view = inflater.inflate(R.layout.device_item, null);
	      view.setTag(new ViewHolder(view));
	    }
	    return view;
	  }
*/
	  static class ViewHolder {
	    final TextView macTextView;
	    final TextView uuidTextView;
	    final TextView majorTextView;
	    final TextView minorTextView;
	    final TextView measuredPowerTextView;
	    final TextView rssiTextView;

	    ViewHolder(View view) {
	      macTextView = (TextView) view.findViewWithTag("mac");
	      uuidTextView = (TextView) view.findViewWithTag("uuid");
	      majorTextView = (TextView) view.findViewWithTag("major");
	      minorTextView = (TextView) view.findViewWithTag("minor");
	      measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
	      rssiTextView = (TextView) view.findViewWithTag("rssi");
	    }
	  }
}
