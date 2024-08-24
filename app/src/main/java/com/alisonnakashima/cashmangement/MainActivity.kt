package com.alisonnakashima.cashmangement

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alisonnakashima.cashmangement.database.DatabaseHandler
import com.alisonnakashima.cashmangement.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import com.alisonnakashima.cashmangement.entity.Insertions
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: SQLiteDatabase

    private lateinit var spType: Spinner
    private lateinit var spDetail: Spinner

    private lateinit var etDateSelected: EditText
    private val calendar = Calendar.getInstance()

    private lateinit var db: DatabaseHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(5000)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHandler(this)
        etDateSelected = binding.etDate

        //Por padrão está setando o dia atual do dispositivo, caso altere a data/hora manualmente, será alterado o dia exibido
        var getDate = getCurrentDate()
        System.out.println(getDate)
        etDateSelected.setText(getDate)

        setButtonListener()

        banco = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath("db-insertions.sqlite"),
            null
        )


        banco.execSQL("CREATE TABLE IF NOT EXISTS insertions (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, date TEXT, description TEXT, value TEXT)")

    }

    private fun setDate(){
        etDateSelected.setOnClickListener {

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog (
                this, {view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                    etDateSelected.setText(dat)
                },
                year,month,day
            )
            datePickerDialog.show()
        }
    }

    private fun setButtonListener() {

        binding.btInsert.setOnClickListener {
            btInsertOnClick()
        }

        binding.btEntryList.setOnClickListener {
            btEntryListOnClick()
        }

        binding.btBalance.setOnClickListener {
            btBalanceOnClick()
        }

        binding.etDate.setOnClickListener{
            setDate()
        }
    }

    private fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd'/'M'/'yyyy")
        return sdf.format(Date())
    }

    private fun btInsertOnClick() {
        if (binding.etValue.text.toString().isEmpty() || binding.etValue.text.toString().toDouble() == 0.0 || binding.etValue.text.toString() == "") {
            binding.etValue.error = getString(R.string.ValueError)
            binding.etValue.requestFocus()

        }

        else if (binding.etDate.text.toString().isEmpty() ) {
            binding.etValue.error = getString(R.string.DateError)
            binding.etValue.requestFocus()
        }

        else {
            val registro = ContentValues()
            registro.put("type", binding.spType.selectedItem.toString())
            registro.put("date", binding.etDate.text.toString())
            registro.put("description", binding.spDetail.selectedItem.toString())
            registro.put("value",binding.etValue.text.toString())

            //Função para arredondar valor inserido para 2 casas decimais
            //REMOVER CASO ENCONTRE A FUNÇÃO QUE LIMITE JÁ NA INSERÇÃO DO VALOR
            var aux = binding.etValue.text.toString().toDouble()
            aux = (aux * 100)
            var aux2 = aux.toInt()
            var aux3 = aux2.toDouble()
            aux3 = (aux3 * 0.01)
            System.out.println(aux3)

            db.insert(Insertions(0, binding.spType.selectedItem.toString(), binding.etDate.text.toString(), binding.spDetail.selectedItem.toString(), aux3.toString() ) )

            System.out.println(registro.toString())
            Toast.makeText(this, getString(R.string.ToastSucessText), Toast.LENGTH_LONG).show()
        }
    }

    private fun btEntryListOnClick() {
        System.out.println("Ver listas")
    }

    private fun btBalanceOnClick() {
        System.out.println("Ver Saldo")
    }

    override fun onPause() {
        super.onPause()
        System.out.println("onPause executado")
    }

    override fun onStop() {
        super.onStop()
        System.out.println("onStop executado")
    }

    override fun onDestroy() {
        super.onDestroy()
        System.out.println("onDestroy executado")
    }

    override fun onRestart() {
        super.onRestart()
        System.out.println("onRestart executado")
    }
}

