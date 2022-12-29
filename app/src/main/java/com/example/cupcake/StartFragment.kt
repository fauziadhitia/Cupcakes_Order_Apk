/*
 * Copyright (C) 2020 The Android Open Source Project
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
package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentStartBinding
import com.example.cupcake.model.OrderViewModel

//Ini adalah layar pertama aplikasi Cupcake. Pengguna dapat memilih berapa banyak cupcake yang akan dipesan.
class StartFragment : Fragment() {

    // Mengikat instance objek yang sesuai dengan tata letak fragment_start.xml
    // Properti ini bukan nol antara callback lifecycle onCreateView() dan onDestroyView(),
    // saat hierarki tampilan dilampirkan ke fragmen.
    private var binding: FragmentStartBinding? = null

    // Gunakan delegasi properti Kotlin 'by activityViewModels()' dari artefak fragmen-ktx
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.startFragment = this
    }

    //Mulai pesanan dengan jumlah cupcakes yang diinginkan dan arahkan ke layar berikutnya.
    fun orderCupcake(quantity: Int) {
        //// Perbarui model tampilan dengan kuantitas
        sharedViewModel.setQuantity(quantity)

        // Jika belum ada varian yang disetel dalam model tampilan, pilih vanilla sebagai varian default
        if (sharedViewModel.hasNoFlavorSet()) {
            sharedViewModel.setFlavor(getString(R.string.vanilla))
        }

        // Arah menuju ke tujuan berikutnya untuk memilih rasa kue mangkuk
        findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
    }

    //Metode lifestyle fragmen ini dipanggil saat hierarki tampilan terkait dengan fragmen
    //sedang dihapus. Akibatnya, bersihkan objek yang mengikat.
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}