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

public class PhrasesActivity extends AppCompatActivity {

    ArrayList<Word> phrasesList = new ArrayList<Word>();
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

        phrasesList.add(new Word(R.string.phrase_where_are_you_going, R.string.miwok_phrase_where_are_you_going, R.raw.phrase_where_are_you_going));
        phrasesList.add(new Word(R.string.phrase_what_is_your_name, R.string.miwok_phrase_what_is_your_name, R.raw.phrase_what_is_your_name));
        phrasesList.add(new Word(R.string.phrase_my_name_is, R.string.miwok_phrase_my_name_is, R.raw.phrase_my_name_is));
        phrasesList.add(new Word(R.string.phrase_how_are_you_feeling, R.string.miwok_phrase_how_are_you_feeling, R.raw.phrase_how_are_you_feeling));
        phrasesList.add(new Word(R.string.phrase_im_feeling_good, R.string.miwok_phrase_im_feeling_good, R.raw.phrase_im_feeling_good));
        phrasesList.add(new Word(R.string.phrase_are_you_coming, R.string.miwok_phrase_are_you_coming, R.raw.phrase_are_you_coming));
        phrasesList.add(new Word(R.string.phrase_yes_im_coming, R.string.miwok_phrase_yes_im_coming, R.raw.phrase_yes_im_coming));
        phrasesList.add(new Word(R.string.phrase_im_coming, R.string.miwok_phrase_im_coming, R.raw.phrase_im_coming));
        phrasesList.add(new Word(R.string.phrase_lets_go, R.string.miwok_phrase_lets_go, R.raw.phrase_lets_go));
        phrasesList.add(new Word(R.string.phrase_come_here, R.string.miwok_phrase_come_here, R.raw.phrase_come_here));

        ListView listView = findViewById(R.id.list);
        WordAdapter itemsAdapter = new WordAdapter(this, phrasesList, R.color.category_phrases);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = phrasesList.get(position);

                releaseMediaPlayer();

                // Request audio focus for playback
                final int result = mAudioManager.requestAudioFocus(mChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());
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
