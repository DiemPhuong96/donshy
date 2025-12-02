package com.example.donshy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.donshy.databinding.ActivityMainBinding
import com.example.donshy.ui.home.HomeFragment
import com.example.donshy.ui.listening.ListeningFragment
import com.example.donshy.ui.profile.ProfileFragment
import com.example.donshy.ui.translate.TranslateFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewpager = binding.viewPager
        val tabLayout = binding.tabLayout
        val fragments: Map<String, Fragment> = mapOf(
            "Home" to HomeFragment(), "Listening" to ListeningFragment(),
            "Translate" to TranslateFragment(), "Profile" to ProfileFragment()
        )
        val viewPagerAdapter = ViewPagerAdapter(this, fragments.values.toList())
        viewpager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = fragments.keys.toList()[position]
        }.attach()
    }
}