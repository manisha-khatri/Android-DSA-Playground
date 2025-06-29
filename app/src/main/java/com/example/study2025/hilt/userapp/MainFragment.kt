package com.example.study2025.hilt.userapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.study2025.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainFragment: Fragment() {

    @Inject
    @FirebaseQualifier
    lateinit var userRepository: UserRepository

/*    @Inject
    @Named("sql")
    lateinit var userRepository: UserRepository*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userRepository.saveUser("manisha@gmail.com", "12345")

        return inflater.inflate(R.layout.fragment_example, container, false)
    }
}