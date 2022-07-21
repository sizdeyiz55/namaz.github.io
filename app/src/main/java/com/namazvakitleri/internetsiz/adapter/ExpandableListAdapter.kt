package com.namazvakitleri.internetsiz.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.namazvakitleri.internetsiz.R

class ExpandableListAdapter(var context: Context,
                            var listDataHeader: ArrayList<String>,
                            var listChildData: HashMap<String, List<String>>): BaseExpandableListAdapter() {


    override fun getGroupCount(): Int {
        return listDataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listChildData[listDataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return listDataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listChildData[listDataHeader[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
       return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {

        var headerTitle = getGroup(groupPosition) as String
        var view: View? = convertView

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_expandable_list_group, null)
        }

        var textListChild = view?.findViewById<TextView>(R.id.lblListHeader)
        textListChild?.typeface = Typeface.DEFAULT_BOLD
        textListChild?.text = headerTitle

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {

        var childText = getChild(groupPosition, childPosition) as String
        var view: View? = convertView

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_expandable_list_item, null)
        }

        var textListChild = view?.findViewById<TextView>(R.id.lblListItem)
        textListChild?.text = childText

        return view
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}