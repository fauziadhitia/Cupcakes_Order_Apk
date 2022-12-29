
package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//Harga untuk satu cupcake
private const val PRICE_PER_CUPCAKE = 2.00

//Biaya tambahan untuk pengambilan pesanan di hari yang sama */
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

// [OrderViewModel] menyimpan informasi tentang pesanan cupcake dalam hal jumlah, rasa, dan
// * tanggal pengambilan. Itu juga tahu bagaimana menghitung harga total berdasarkan detail pesanan ini.
class OrderViewModel : ViewModel() {

    // Jumlah cupcakes dalam urutan ini
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // Rasa cupcake untuk pesanan ini
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // Opsi tanggal yang memungkinkan
    val dateOptions: List<String> = getPickupOptions()

    // tanggal pengambilan
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Harga pesanan sejauh ini
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        // Format harga ke dalam mata uang lokal dan kembalikan ini sebagai LiveData<String>
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        // Tetapkan nilai awal untuk pesanan
        resetOrder()
    }

    //Tetapkan jumlah cupcakes untuk pesanan ini.
    //
    //@param numberCupcakes untuk dipesan
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

   // Atur rasa cupcakes untuk pesanan ini. Hanya 1 rasa yang dapat dipilih untuk seluruh pesanan.
   //
   //   @param yang diinginkanFlavour adalah rasa cupcake sebagai string
   //
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

   // Tetapkan tanggal pengambilan untuk pesanan ini.
   //
   //@param pickupDate adalah tanggal pengambilan sebagai string
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    //Mengembalikan nilai true jika rasa belum dipilih untuk pesanan. Mengembalikan salah jika tidak.
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    //Atur ulang pesanan dengan menggunakan nilai default awal untuk kuantitas, rasa, tanggal, dan harga.
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    //Memperbarui harga berdasarkan detail pesanan.
    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // If the user selected the first option (today) for pickup, add the surcharge
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    //Mengembalikan daftar opsi tanggal dimulai dengan tanggal saat ini dan 3 tanggal berikutnya.
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}