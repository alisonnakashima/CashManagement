package com.alisonnakashima.cashmangement.Adapter

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alisonnakashima.cashmangement.R
import com.alisonnakashima.cashmangement.entity.Insertions
import com.alisonnakashima.cashmangement.database.DatabaseHandler.Companion.ID
import com.alisonnakashima.cashmangement.database.DatabaseHandler.Companion.TYPE
import com.alisonnakashima.cashmangement.database.DatabaseHandler.Companion.DATE
import com.alisonnakashima.cashmangement.database.DatabaseHandler.Companion.DESCRIPTION
import com.alisonnakashima.cashmangement.database.DatabaseHandler.Companion.VALUE

class ListAdapter(val context: Context, val cursor: Cursor): BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        val insertions = Insertions(
            cursor.getInt(ID),
            cursor.getString(TYPE),
            cursor.getString(DATE),
            cursor.getString(DESCRIPTION),
            cursor.getString(VALUE)
        )
        return insertions
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getLong(ID)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ) as LayoutInflater
        val v = inflater.inflate(R.layout.activity_insertions_list, null)


        val tvInsertionShow = v.findViewById<TextView>(R.id.tvStringElement)
        val model = v.findViewById<ConstraintLayout>(R.id.model)

        cursor.moveToPosition(position)

        //alterar cor de acordo com inserção do tipo crédito ou débito
        if( cursor.getString(TYPE) == "Débito" ) {
            model.setBackgroundColor( Color.parseColor("#CD7676") )
        }
        else {
            model.setBackgroundColor( Color.parseColor( "#FFA5D6A7" ) )
        }

        val aux = (cursor.getString(TYPE) + " - " + cursor.getString(DATE) + " - " + cursor.getString(
            DESCRIPTION) + " - " + cursor.getString(VALUE))

        tvInsertionShow.setText(aux)
        System.out.println(aux)
        return v
    }

}