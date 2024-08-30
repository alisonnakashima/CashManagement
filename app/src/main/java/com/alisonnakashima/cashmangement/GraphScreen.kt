package com.alisonnakashima.cashmangement

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.alisonnakashima.cashmangement.databinding.ActivityGraphScreenBinding
import com.alisonnakashima.cashmangement.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.alisonnakashima.cashmangement.MainActivity
import com.alisonnakashima.cashmangement.database.DatabaseHandler
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.*


class GraphScreen : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private lateinit var barDataSet2: BarDataSet
    private lateinit var barEntriesList: ArrayList<BarEntry>
    private lateinit var barEntriesList2: ArrayList<BarEntry>
    private lateinit var banco: DatabaseHandler
    private lateinit var round: MainActivity
    private lateinit var legend: Legend


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_screen)
        banco = DatabaseHandler(this)

        barChart = findViewById(R.id.chart)
        barEntriesList = ArrayList()
        barEntriesList2 = ArrayList()
        legend = Legend()

        barChart.axisRight.setDrawLabels(false)
        barChart.xAxis.setEnabled(false)
        barChart.axisLeft.setEnabled(true)
        barChart.axisRight.setEnabled(false)
        barChart.axisLeft.setDrawLabels(false)
        barChart.setDrawGridBackground(false)
        barChart.xAxis.setDrawLabels(false)
        barChart.axisLeft.setDrawZeroLine(true)


        round = MainActivity()

        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)

        var values = banco.graphData()

        val aux1= round.round2DecimalPlaces(values[0].toDouble())
        val aux2 =round.round2DecimalPlaces(values[1].toDouble())

        barChart.axisLeft.setAxisMinimum(0f)

        barEntriesList.add(BarEntry(1f, aux1.toFloat()))
        barDataSet = BarDataSet(barEntriesList, "Crédito")

        barEntriesList2.add(BarEntry(2f, aux2.toFloat()))
        barDataSet2 = BarDataSet(barEntriesList2, "Débito")

        barData = BarData(barDataSet,barDataSet2)

        barDataSet.setValueTextSize(16f)
        barDataSet2.setValueTextSize(16f)
        barData.setValueTextColor(Color.BLACK)
        barDataSet.setColors(colors[0])
        barDataSet2.setColors(colors[1])
        barData.setValueTextSize(16f)

        barChart.setDrawGridBackground(false)
        barChart.setScaleEnabled(false)
        barChart.description.isEnabled = false


        legend = barChart.getLegend()
        legend.setTextSize(16f)

        barChart.data = barData

    }

    private fun max (a: Float, b: Float): Float {
        var aux = (maxOf(a.toInt(), b.toInt())).toDouble()
        var counter = 0
        while (aux > 1){
            counter += 1
            aux += aux*0.1
            aux += aux/10
        }

        for (counter in counter .. 0){
            aux += aux*10
        }
        System.out.println(aux.toString()+" fun Max")
        return(aux.toFloat())
    }

}