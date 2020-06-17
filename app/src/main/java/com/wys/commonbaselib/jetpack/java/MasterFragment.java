package com.wys.commonbaselib.jetpack.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:主fragent（显示列表）
 */
class MasterFragment extends Fragment {
    private SharedViewModel shareModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shareModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Button itemSelector = new Button(requireContext());
        itemSelector.setOnClickListener(v -> shareModel.select("str"));
    }
}
