package com.wys.commonbaselib.jetpack.java;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:详情fragment
 */
class DetailFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel sharedModule = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedModule.getSelected().observe(getViewLifecycleOwner(), s -> {
            //update selected item
        });
    }
}
