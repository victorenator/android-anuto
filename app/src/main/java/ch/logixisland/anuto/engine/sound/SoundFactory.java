package ch.logixisland.anuto.engine.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class SoundFactory {

    private static final int MAX_STREAMS = 8;

    private final Context mContext;
    private final SoundPool mSoundPool;
    private final Map<Integer, Integer> mSoundMap = new HashMap<>();

    public SoundFactory(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }

    public Sound createSound(int resId) {
        if (!mSoundMap.containsKey(resId)) {
            int soundId = mSoundPool.load(mContext, resId, 0);
            mSoundMap.put(resId, soundId);
        }

        return new Sound(mSoundPool, mSoundMap.get(resId));
    }

}