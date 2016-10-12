package com.maxtain.bebe.util;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.content.Context;

public class AnimationSound {
	MediaPlayer mpsound;

	public AnimationSound(Context context, int id) {

		// Creates an instance of the mediaplayer using the passed mp3 file

		mpsound = MediaPlayer.create(context, id);

	}

	// When this method is called, the sound is played in a loop until the
	// stopsound method is called

	public void startsound(boolean loop) {
		mpsound.start();
		mpsound.setLooping(loop);

	}

	// When this method is called , the playing of the sound is stopped

	public void stopsound() {
		if (mpsound != null) {
			if (mpsound.isPlaying()) {
				mpsound.stop();
				mpsound.setLooping(false);
			}

			// Finally release everything and clear out the mediaplayer object

			mpsound.release();
			mpsound = null;
		}

	}
}