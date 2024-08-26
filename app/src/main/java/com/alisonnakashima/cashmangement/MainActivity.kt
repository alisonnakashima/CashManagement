package com.alisonnakashima.cashmangement

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alisonnakashima.cashmangement.Adapter.ListAdapter
import com.alisonnakashima.cashmangement.database.DatabaseHandler
import com.alisonnakashima.cashmangement.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Date
import com.alisonnakashima.cashmangement.entity.Inserter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase

    private lateinit var spType: Spinner
    private lateinit var spDetail: Spinner

    private lateinit var etDateSelected: EditText
    private val calendar = Calendar.getInstance()

    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(5000)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        banco = DatabaseHandler(this)
        etDateSelected = binding.etDate


        //Por padrão está setando o dia atual do dispositivo, caso altere a data/hora manualmente, será alterado o dia exibido
        var getDate = getCurrentDate()
        etDateSelected.setText(getDate)

        setButtonListener()

        db = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath("db-insertions.sqlite"),
            null )


        //Alterar componentes do campo spDetails de acordo com o que está selecionado em spType
        spDetail = findViewById(R.id.spDetail)
        spType = findViewById(R.id.spType)

        val detailsCredit= resources.getStringArray(R.array.DetailsCredit).toList()
        val detailsDebit = resources.getStringArray(R.array.DetailsDebit).toList()
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, detailsCredit)


        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long)
                {

                    if( binding.spType.selectedItem.toString() == "Crédito" ){
                        System.out.println(binding.spType.selectedItem.toString())
                        adapter = ArrayAdapter<String>(baseContext, android.R.layout.simple_list_item_1, detailsCredit)
                        spDetail.setAdapter(adapter)
                    }
                    else {
                        adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, detailsDebit)
                        spDetail.setAdapter (adapter)
                    }

                }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
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
            var aux = round2DecimalPlaces(binding.etValue.text.toString().toDouble())
//            aux = (aux * 100)
//            var aux2 = aux.toInt()
//            var aux3 = aux2.toDouble()
//            aux3 = (aux3 * 0.01)
            System.out.println(aux)

            banco.insert(Inserter(0, binding.spType.selectedItem.toString(), binding.etDate.text.toString(), binding.spDetail.selectedItem.toString(), aux.toString() ) )

            System.out.println( binding.spType.selectedItem.toString() +" "+ binding.etDate.text.toString() +" "+ binding.spDetail.selectedItem.toString() +" R$"+ aux.toString() )
            Toast.makeText(this, getString(R.string.ToastSucessText), Toast.LENGTH_LONG).show()
        }
    }

    private fun btEntryListOnClick() {
        val intent = Intent (this, ShowInsertions::class.java )
        System.out.println("Ver listas")
        startActivity( intent )
    }

    private fun btBalanceOnClick() {

        banco = DatabaseHandler(this)

        val balance = round2DecimalPlaces(banco.balance()).toString()

        if ( balance.toDouble() > 0 ) {
//            binding.tvBalanceResult.setBackgroundColor(Color.parseColor("#FF87C889") )
            var toast = Toast.makeText(this, "R$ " + balance, Toast.LENGTH_LONG)
            var view = toast.getView()
            if (view != null) {
                view.setBackgroundColor(Color.parseColor("#FF87C889"))
            }
            view = view as ViewGroup
            val aux = view.getChildAt(0) as TextView
            aux.setTextSize(26F)
            toast.show()
        }

        else if (balance.toDouble() < 0 ) {
//            binding.tvBalanceResult.setBackgroundColor(Color.parseColor("#FFBC9292") )
            var toast = Toast.makeText(this, "R$ " + balance, Toast.LENGTH_LONG)
            var view = toast.getView()
            if (view != null) {
                view.setBackgroundColor(Color.parseColor("#FFBC9292"))
            }
            view = view as ViewGroup
            val aux = view.getChildAt(0) as TextView
            aux.setTextSize(26F)
            toast.show()
        }

        else {
//            binding.tvBalanceResult.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            var toast = Toast.makeText(this, "R$ " + balance, Toast.LENGTH_LONG)
            var view = toast.getView()
            if (view != null) {
                view.setBackgroundColor(Color.parseColor("#FFFC00"))
            }
            view = view as ViewGroup
            val aux = view.getChildAt(0) as TextView
            aux.setTextSize(26F)
            toast.show()
        }

        System.out.println("Ver Saldo")
    }

    fun round2DecimalPlaces ( x: Double) : Double {
        //Função para arredondar valor inserido para 2 casas decimais
        //REMOVER CASO ENCONTRE A FUNÇÃO QUE LIMITE JÁ NA INSERÇÃO DO VALOR
        var aux = (x * 100)
        var aux2 = aux.toInt()
        var aux3 = aux2.toDouble()
        aux3 = (aux3 * 0.01)
        return aux3
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

