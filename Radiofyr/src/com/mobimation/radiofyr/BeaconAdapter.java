package com.mobimation.radiofyr;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
// Test comment
import com.mobimation.radiofyr.R;
import com.aprilbrother.aprilbrothersdk.Beacon;

public class BeaconAdapter extends BaseAdapter {

	private ArrayList<Beacon> beacons;
	private LayoutInflater inflater;

	public BeaconAdapter(Context context,ArrayList<Beacon> beacons) {
		this.inflater = LayoutInflater.from(context);
	    this.beacons = beacons;
	}
	
	public void replaceWith(Collection<Beacon> newBeacons) {
	    this.beacons.clear();
	    this.beacons.addAll(newBeacons);
	    notifyDataSetChanged();
	  }

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

	  @Override
	  public View getView(int position, View view, ViewGroup parent) {
	    view = inflateIfRequired(view, position, parent);
	    bind(getItem(position), view);
	    return view;
	  }

	  private void bind(Beacon beacon, View view) {
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.macTextView.setText(String.format("MAC: %s (%.2fm)", beacon.getMacAddress(), beacon.getDistance()));
	    holder.uuidTextView.setText("Identitet: "+ beacon.getProximityUUID());
	    holder.majorTextView.setText("Plats: " + beacon.getMajor());
	    holder.minorTextView.setText("Position: " + beacon.getMinor());
	    holder.measuredPowerTextView.setText("Batteri: " + beacon.getMeasuredPower());
	    holder.rssiTextView.setText("Signalstyrka: " + beacon.getRssi());
	  }

	  private View inflateIfRequired(View view, int position, ViewGroup parent) {
	    if (view == null) {
	      view = inflater.inflate(R.layout.device_item, null);
	      view.setTag(new ViewHolder(view));
	    }
	    return view;
	  }

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
