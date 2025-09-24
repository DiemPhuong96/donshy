package com.example.donshy.ui.listening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donshy.data.model.AudioType
import com.example.donshy.databinding.ListeningFragmentBinding
import com.example.donshy.ui.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ListeningFragment : Fragment() {
    private var _binding: ListeningFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListeningFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listeningTopics = AudioType.entries.map { it.type }
        val fragments: List<Fragment> = listeningTopics.map { AudioFragment.newInstance(it) }
        val viewPagerAdapter = ViewPagerAdapter(requireActivity(), fragments)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listeningTopics[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}