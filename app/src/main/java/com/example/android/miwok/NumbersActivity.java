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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    ArrayList<Word> numbersList = new ArrayList<Word>();
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

        numbersList.add(new Word(R.string.number_one, R.string.miwok_number_one, R.drawable.number_one, R.raw.number_one));
        numbersList.add(new Word(R.string.number_two, R.string.miwok_number_two, R.drawable.number_two, R.raw.number_two));
        numbersList.add(new Word(R.string.number_three, R.string.miwok_number_three, R.drawable.number_three, R.raw.number_three));
        numbersList.add(new Word(R.string.number_four, R.string.miwok_number_four, R.drawable.number_four, R.raw.number_four));
        numbersList.add(new Word(R.string.number_five, R.string.miwok_number_five, R.drawable.number_five, R.raw.number_five));
        numbersList.add(new Word(R.string.number_six, R.string.miwok_number_six, R.drawable.number_six, R.raw.number_six));
        numbersList.add(new Word(R.string.number_seven, R.string.miwok_number_seven, R.drawable.number_seven, R.raw.number_seven));
        numbersList.add(new Word(R.string.number_eight, R.string.miwok_number_eight, R.drawable.number_eight, R.raw.number_eight));
        numbersList.add(new Word(R.string.number_nine, R.string.miwok_number_nine, R.drawable.number_nine, R.raw.number_nine));
        numbersList.add(new Word(R.string.number_ten, R.string.miwok_number_ten, R.drawable.number_ten, R.raw.number_ten));

        ListView listView = findViewById(R.id.list);
        WordAdapter itemsAdapter = new WordAdapter(this, numbersList, R.color.category_numbers);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = numbersList.get(position);

                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());
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
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mChangeListener);
        }
    }
}
