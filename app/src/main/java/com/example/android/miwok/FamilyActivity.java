/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    ArrayList<Word> familyList = new ArrayList<Word>();
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mMediaPlayer.stop();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        familyList.add(new Word(R.string.family_father, R.string.miwok_family_father, R.drawable.family_father, R.raw.family_father));
        familyList.add(new Word(R.string.family_mother, R.string.miwok_family_mother, R.drawable.family_mother, R.raw.family_mother));
        familyList.add(new Word(R.string.family_son, R.string.miwok_family_son, R.drawable.family_son, R.raw.family_son));
        familyList.add(new Word(R.string.family_daughter, R.string.miwok_family_daughter, R.drawable.family_daughter, R.raw.family_daughter));
        familyList.add(new Word(R.string.family_older_brother, R.string.miwok_family_older_brother, R.drawable.family_older_brother, R.raw.family_older_brother));
        familyList.add(new Word(R.string.family_younger_brother, R.string.miwok_family_younger_brother, R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyList.add(new Word(R.string.family_older_sister, R.string.miwok_family_older_sister, R.drawable.family_older_sister, R.raw.family_older_brother));
        familyList.add(new Word(R.string.family_younger_sister, R.string.miwok_family_younger_sister, R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyList.add(new Word(R.string.family_grandmother, R.string.miwok_family_grandmother, R.drawable.family_grandmother, R.raw.family_grandmother));
        familyList.add(new Word(R.string.family_grandfather, R.string.miwok_family_grandfather, R.drawable.family_grandfather, R.raw.family_grandfather));

        ListView listView = findViewById(R.id.list);
        WordAdapter itemsAdapter = new WordAdapter(this, familyList, R.color.category_family);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = familyList.get(position);

                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMediaPlayer();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mChangeListener);
        }
    }
}
