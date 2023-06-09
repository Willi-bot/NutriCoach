package de.uniaugsburg.app.ui.items;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.uniaugsburg.app.R;
import de.uniaugsburg.app.databinding.FragmentItemsBinding;

import de.uniaugsburg.app.util.JsonParser;


public class ItemsFragment extends Fragment {

    private FragmentItemsBinding binding;
    private ListView listView;

    List<Map<String, String>> dataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ItemsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);

        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.list_view);
        updateData(false);

        return root;
    }

    private void updateData(boolean fromAsset) {
        Map<String, List<Integer>> itemKcalMap;
        if(fromAsset) {
            itemKcalMap = JsonParser.parseJsonFromAsset(this.requireContext());
        }else {
            itemKcalMap = JsonParser.parseJson(this.requireContext());
        }
        // Create the list adapter
        dataList = new ArrayList<>();

        List<String> sortedKeys = itemKcalMap.keySet()
                .stream()
                .sorted(Comparator.comparingInt((String s) -> Integer.parseInt(s.split(" ")[0])).reversed())
                .collect(Collectors.toList());
        for (String key : sortedKeys) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("item_name", key);
            dataMap.put("item_kcal", (Objects.requireNonNull(itemKcalMap.get(key)).get(0) + " kcal"));
            dataList.add(dataMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), dataList,
                android.R.layout.simple_list_item_2,
                new String[] {"item_name", "item_kcal"},
                new int[] {android.R.id.text1, android.R.id.text2});

        // Set the list adapter
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {

        updateData(false);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}