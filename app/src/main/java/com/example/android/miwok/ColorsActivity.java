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

public class ColorsActivity extends AppCompatActivity {

    ArrayList<Word> colorsList = new ArrayList<Word>();
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

        colorsList.add(new Word(R.string.color_red, R.string.miwok_color_red, R.drawable.color_red, R.raw.color_red));
        colorsList.add(new Word(R.string.color_dusty_yellow, R.string.miwok_color_dusty_yellow, R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorsList.add(new Word(R.string.color_mustard_yellow, R.string.miwok_color_mustard_yellow, R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        colorsList.add(new Word(R.string.color_green, R.string.miwok_color_green, R.drawable.color_green, R.raw.color_green));
        colorsList.add(new Word(R.string.color_brown, R.string.miwok_color_brown, R.drawable.color_brown, R.raw.color_brown));
        colorsList.add(new Word(R.string.color_black, R.string.miwok_color_black, R.drawable.color_black, R.raw.color_black));
        colorsList.add(new Word(R.string.color_gray, R.string.miwok_color_gray, R.drawable.color_gray, R.raw.color_gray));
        colorsList.add(new Word(R.string.color_white, R.string.miwok_color_white, R.drawable.color_white, R.raw.color_white));

        ListView listView = findViewById(R.id.list);
        WordAdapter itemsAdapter = new WordAdapter(this, colorsList, R.color.category_colors);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = colorsList.get(position);

                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceId());
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
