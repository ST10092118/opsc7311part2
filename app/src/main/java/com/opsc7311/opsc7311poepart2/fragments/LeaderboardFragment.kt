package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.LeaderboardAdapter
import com.opsc7311.opsc7311poepart2.database.service.LeaderboardService
import com.opsc7311.opsc7311poepart2.viewmodel.LeaderboardViewModel


class LeaderboardFragment : Fragment() {

    private val leaderboardViewModel: LeaderboardViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private lateinit var leaderboardAdapter: LeaderboardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        leaderboardAdapter = LeaderboardAdapter()

        recyclerView = view.findViewById(R.id.leaderboard_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = leaderboardAdapter

        getLeaderboard()

        return view
    }

    private fun getLeaderboard(){
        leaderboardViewModel._leaderboard.observe(viewLifecycleOwner){
            // This method was adapted from stackoverflow
            // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
            // CommonsWare
            // https://stackoverflow.com/users/115145/commonsware

                leaderboard ->
            Log.d("error", leaderboard.toString())

            leaderboardAdapter.updateData(leaderboard)
        }

        leaderboardViewModel.error.observe(viewLifecycleOwner){
            err -> Log.d("error", err)
        }



        leaderboardViewModel.fetchLeaderboard()
    }
}