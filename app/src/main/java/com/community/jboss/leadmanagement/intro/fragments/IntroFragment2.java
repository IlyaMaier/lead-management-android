package com.community.jboss.leadmanagement.intro.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.community.jboss.leadmanagement.R;

public class IntroFragment2 extends Fragment {

    public IntroFragment2() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_fragment2, container, false);
    }

}
