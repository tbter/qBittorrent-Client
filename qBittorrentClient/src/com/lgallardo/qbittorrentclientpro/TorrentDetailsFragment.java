/*******************************************************************************
 * Copyright (c) 2014 Luis M. Gallardo D..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Luis M. Gallardo D. 
 ******************************************************************************/
package com.lgallardo.qbittorrentclientpro;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TorrentDetailsFragment extends Fragment {

	// Torrent variables
	String name, info, hash, ratio, size, state, leechs, seeds, progress, priority, savePath, creationDate, comment, totalWasted, totalUploaded,
			totalDownloaded, timeElapsed, nbConnections, shareRatio, uploadRateLimit, downloadRateLimit, downloaded, eta, downloadSpeed, uploadSpeed,
			percentage = "";

	String url;

	int position;

	JSONObject json2;

	public TorrentDetailsFragment() {
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		// Tell the host activity that your fragment has menu options that it
		// wants to add/replace/delete using the onCreateOptionsMenu method.
		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.torrent_details, container, false);

		// Log.i("TorrentDetails", "Position =>>> " + position);

		savePath = "";
		creationDate = "";
		comment = "";
		uploadRateLimit = "";
		downloadRateLimit = "";
		totalWasted = "";
		totalUploaded = "";
		totalDownloaded = "";
		timeElapsed = "";
		nbConnections = "";
		shareRatio = "";

		try {

			if (savedInstanceState != null) {

				// Get saved values

				name = savedInstanceState.getString("torrentDetailName", "");
				size = savedInstanceState.getString("torrentDetailSize", "");
				hash = savedInstanceState.getString("torrentDetailHash", "");
				ratio = savedInstanceState.getString("torrentDetailRatio", "");
				state = savedInstanceState.getString("torrentDetailState", "");
				leechs = savedInstanceState.getString("torrentDetailLeechs", "");
				seeds = savedInstanceState.getString("torrentDetailSeeds", "");
				progress = savedInstanceState.getString("torrentDetailProgress", "");
				priority = savedInstanceState.getString("torrentDetailPriority", "");
				eta = savedInstanceState.getString("torrentDetailEta", "");
				uploadSpeed = savedInstanceState.getString("torrentDetailUploadSpeed", "");
				downloadSpeed = savedInstanceState.getString("torrentDetailDownloadSpeed", "");
				downloaded = savedInstanceState.getString("torrentDetailDownloaded", "");

				int index = progress.indexOf(".");

				if (index == -1) {
					index = progress.indexOf(",");

					if (index == -1) {
						index = progress.length();
					}
				}

				percentage = progress.substring(0, index);

				// Log.i("savedInstanceState", "The state is: " +state);
				// Log.i("savedInstanceState", "The percentage is: "
				// +percentage);

				// return rootView;

			} else {

				// Get values from current activity
				name = MainActivity.lines[position].getFile();
				size = MainActivity.lines[position].getSize();
				hash = MainActivity.lines[position].getHash();
				ratio = MainActivity.lines[position].getRatio();
				state = MainActivity.lines[position].getState();
				leechs = MainActivity.lines[position].getLeechs();
				seeds = MainActivity.lines[position].getSeeds();
				progress = MainActivity.lines[position].getProgress();
				priority = MainActivity.lines[position].getPriority();
				eta = MainActivity.lines[position].getEta();
				uploadSpeed = MainActivity.lines[position].getUploadSpeed();
				downloadSpeed = MainActivity.lines[position].getDownloadSpeed();
				downloaded = MainActivity.lines[position].getDownloaded();

				int index = MainActivity.lines[position].getProgress().indexOf(".");

				if (index == -1) {
					index = MainActivity.lines[position].getProgress().indexOf(",");

					if (index == -1) {
						index = MainActivity.lines[position].getProgress().length();
					}
				}

				percentage = MainActivity.lines[position].getProgress().substring(0, index);
			}

			TextView nameTextView = (TextView) rootView.findViewById(R.id.torrentName);
			TextView sizeTextView = (TextView) rootView.findViewById(R.id.downloadedVsTotal);
			TextView ratioTextView = (TextView) rootView.findViewById(R.id.torrentRatio);
			TextView priorityTextView = (TextView) rootView.findViewById(R.id.torrentPriority);
			TextView stateTextView = (TextView) rootView.findViewById(R.id.torrentState);
			TextView leechsTextView = (TextView) rootView.findViewById(R.id.torrentLeechs);
			TextView seedsTextView = (TextView) rootView.findViewById(R.id.torrentSeeds);
			TextView progressTextView = (TextView) rootView.findViewById(R.id.torrentProgress);
			TextView hashTextView = (TextView) rootView.findViewById(R.id.torrentHash);

			TextView etaTextView = (TextView) rootView.findViewById(R.id.eta);
			TextView uploadSpeedTextView = (TextView) rootView.findViewById(R.id.uploadSpeed);
			TextView downloadSpeedTextView = (TextView) rootView.findViewById(R.id.DownloadSpeed);

			nameTextView.setText(name);
			ratioTextView.setText(ratio);
			stateTextView.setText(state);
			leechsTextView.setText(leechs);
			seedsTextView.setText(seeds);
			progressTextView.setText(progress);
			hashTextView.setText(hash);
			priorityTextView.setText(priority);
			etaTextView.setText(eta);

			downloadSpeedTextView.setText(Character.toString('\u2193') + " " + downloadSpeed);
			uploadSpeedTextView.setText(Character.toString('\u2191') + " " + uploadSpeed);

			// Set Downloaded vs Total size
			sizeTextView.setText(downloaded + " / " + size);

			// Set progress bar
			ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
			TextView percentageTV = (TextView) rootView.findViewById(R.id.percentage);

			progressBar.setProgress(Integer.parseInt(percentage));
			percentageTV.setText(percentage + "%");

			// Set status icon
			ImageView icon = (ImageView) rootView.findViewById(R.id.icon);

			if ("pausedUP".equals(state) || "pausedDL".equals(state)) {
				icon.setImageResource(R.drawable.paused);
			}

			if ("stalledUP".equals(state)) {
				icon.setImageResource(R.drawable.stalledup);
			}

			if ("stalledDL".equals(state)) {
				icon.setImageResource(R.drawable.stalleddl);
			}

			if ("downloading".equals(state)) {
				icon.setImageResource(R.drawable.downloading);
			}

			if ("uploading".equals(state)) {
				icon.setImageResource(R.drawable.uploading);
			}

			if ("queuedDL".equals(state) || "queuedUP".equals(state)) {
				icon.setImageResource(R.drawable.queued);
			}

			// Show progressBar
			if (MainActivity.progressBar != null) {
				MainActivity.progressBar.setVisibility(View.VISIBLE);
			}
			// Execute the task in background
			qBittorrentGeneralInfoTask qgit = new qBittorrentGeneralInfoTask();

			qgit.execute(new View[] { rootView });

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("TorrentDetailsFragment - onCreateView", e.toString());
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("torrentDetailName", name);
		outState.putString("torrentDetailSize", size);
		outState.putString("torrentDetailHash", hash);
		outState.putString("torrentDetailRatio", ratio);
		outState.putString("torrentDetailState", state);
		outState.putString("torrentDetailLeechs", leechs);
		outState.putString("torrentDetailSeeds", seeds);
		outState.putString("torrentDetailProgress", progress);
		outState.putString("torrentDetailPriority", priority);
		outState.putString("torrentDetailEta", eta);
		outState.putString("torrentDetailUploadSpeed", uploadSpeed);
		outState.putString("torrentDetailDownloadSpeed", downloadSpeed);
		outState.putString("torrentDetailDownloaded", downloaded);

	}

	// @Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Log.i("FragmentLIst", "Item touched");
	}

	// @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu != null) {

			if (getActivity().findViewById(R.id.one_frame) != null) {
				menu.findItem(R.id.action_refresh).setVisible(false);
			}
			menu.findItem(R.id.action_search).setVisible(false);
			menu.findItem(R.id.action_resume_all).setVisible(false);
			menu.findItem(R.id.action_pause_all).setVisible(false);
			menu.findItem(R.id.action_add).setVisible(false);

			menu.findItem(R.id.action_resume).setVisible(true);
			menu.findItem(R.id.action_pause).setVisible(true);
			menu.findItem(R.id.action_increase_prio).setVisible(true);
			menu.findItem(R.id.action_decrease_prio).setVisible(true);
			menu.findItem(R.id.action_delete).setVisible(true);
			menu.findItem(R.id.action_delete_drive).setVisible(true);
			menu.findItem(R.id.action_download_rate_limit).setVisible(true);
			menu.findItem(R.id.action_upload_rate_limit).setVisible(true);

		}
	}

	// Here is where the action happens
	private class qBittorrentGeneralInfoTask extends AsyncTask<View, View, View[]> {

		protected View[] doInBackground(View... rootViews) {
			// Get torrent's extra info
			url = "json/propertiesGeneral/";

			try {

				JSONParser jParser = new JSONParser(MainActivity.hostname, MainActivity.subfolder, MainActivity.protocol, MainActivity.port,
						MainActivity.username, MainActivity.password, MainActivity.connection_timeout, MainActivity.data_timeout);

				json2 = jParser.getJSONFromUrl(url + hash);

				MainActivity.lines[position].setSavePath(json2.getString(MainActivity.TAG_SAVE_PATH));
				MainActivity.lines[position].setCreationDate(json2.getString(MainActivity.TAG_CREATION_DATE));
				MainActivity.lines[position].setComment(json2.getString(MainActivity.TAG_COMMENT));
				MainActivity.lines[position].setTotalWasted(json2.getString(MainActivity.TAG_TOTAL_WASTED));
				MainActivity.lines[position].setTotalUploaded(json2.getString(MainActivity.TAG_TOTAL_UPLOADED));
				MainActivity.lines[position].setTotalDownloaded(json2.getString(MainActivity.TAG_TOTAL_DOWNLOADED));
				MainActivity.lines[position].setTimeElapsed(json2.getString(MainActivity.TAG_TIME_ELAPSED));
				MainActivity.lines[position].setNbConnections(json2.getString(MainActivity.TAG_NB_CONNECTIONS));
				MainActivity.lines[position].setShareRatio(json2.getString(MainActivity.TAG_SHARE_RATIO));
				MainActivity.lines[position].setUploadLimit(json2.getString(MainActivity.TAG_UPLOAD_LIMIT));
				MainActivity.lines[position].setDownloadLimit(json2.getString(MainActivity.TAG_DOWNLOAD_LIMIT));

			} catch (Exception e) {

				// MainActivity.lines[position].setSavePath(" ");
				// MainActivity.lines[position].setCreationDate(" ");
				// MainActivity.lines[position].setComment(" ");
				// MainActivity.lines[position].setTotalWasted(" ");
				// MainActivity.lines[position].setTotalUploaded(" ");
				// MainActivity.lines[position].setTotalDownloaded(" ");
				// MainActivity.lines[position].setTimeElapsed(" ");
				// MainActivity.lines[position].setNbConnections(" ");
				// MainActivity.lines[position].setShareRatio(" ");
				// MainActivity.lines[position].setUploadLimit(" ");
				// MainActivity.lines[position].setDownloadLimit(" ");

				Log.e("TorrentFragment:", e.toString());

			}

			return rootViews;

		}

		@Override
		protected void onPostExecute(View[] rootViews) {

			try {

				View rootView = rootViews[0];

				TextView pathTextView, creationDateTextView, commentTextView, uploadRateLimitTextView, downloadRateLimitTextView, totalWastedTextView, totalUploadedTextView, totalDownloadedTextView, timeElapsedTextView, nbConnectionsTextView, shareRatioTextView = null;

				pathTextView = (TextView) rootView.findViewById(R.id.torrentSavePath);
				creationDateTextView = (TextView) rootView.findViewById(R.id.torrentCreationDate);
				commentTextView = (TextView) rootView.findViewById(R.id.torrentComment);
				uploadRateLimitTextView = (TextView) rootView.findViewById(R.id.torrentUploadRateLimit);
				downloadRateLimitTextView = (TextView) rootView.findViewById(R.id.torrentDownloadRateLimit);
				totalWastedTextView = (TextView) rootView.findViewById(R.id.torrentTotalWasted);
				totalUploadedTextView = (TextView) rootView.findViewById(R.id.torrentTotalUploaded);
				totalDownloadedTextView = (TextView) rootView.findViewById(R.id.torrentTotalDownloaded);
				timeElapsedTextView = (TextView) rootView.findViewById(R.id.torrentTimeElapsed);
				nbConnectionsTextView = (TextView) rootView.findViewById(R.id.torrentNbConnections);
				shareRatioTextView = (TextView) rootView.findViewById(R.id.torrentShareRatio);

				Log.i("TorrentFragment - onPostExecute", "position: " + position);

				savePath = MainActivity.lines[position].getSavePath();
				creationDate = MainActivity.lines[position].getCreationDate();
				comment = MainActivity.lines[position].getComment();
				uploadRateLimit = MainActivity.lines[position].getUploadLimit();
				downloadRateLimit = MainActivity.lines[position].getDownloadLimit();
				totalWasted = MainActivity.lines[position].getTotalWasted();
				totalUploaded = MainActivity.lines[position].getTotalUploaded();
				totalDownloaded = MainActivity.lines[position].getTotalDownloaded();
				timeElapsed = MainActivity.lines[position].getTimeElapsed();
				nbConnections = MainActivity.lines[position].getNbConnections();
				shareRatio = MainActivity.lines[position].getShareRatio();

				pathTextView.setText(savePath);
				creationDateTextView.setText(creationDate);
				commentTextView.setText(comment);
				uploadRateLimitTextView.setText(uploadRateLimit);
				downloadRateLimitTextView.setText(downloadRateLimit);
				totalWastedTextView.setText(totalWasted);
				totalUploadedTextView.setText(totalUploaded);
				totalDownloadedTextView.setText(totalDownloaded);
				timeElapsedTextView.setText(timeElapsed);
				nbConnectionsTextView.setText(nbConnections);
				shareRatioTextView.setText(shareRatio);

				if (json2 == null) {
					Toast.makeText(getActivity(), R.string.torrent_details_cant_general_ino, Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block

			}

			// Hide progressBar
			if (MainActivity.progressBar != null) {
				MainActivity.progressBar.setVisibility(View.INVISIBLE);
			}

		}

	}

}