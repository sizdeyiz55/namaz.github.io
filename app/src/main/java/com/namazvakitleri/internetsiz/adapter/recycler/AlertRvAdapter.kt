package com.namazvakitleri.internetsiz.adapter.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.main.Kible
import com.namazvakitleri.internetsiz.main.Zikirmatik
import com.namazvakitleri.internetsiz.utils.constant.Constant


class AlertRvAdapter (val mycontext : Context,
                      val list : ArrayList<String>?,
                      val remaningPrayTime : String?,
                      val afterYatsi : Boolean?,
                      val dua: String?,
                      val duaResource: String?,
                      val ayet: String?,
                      val ayetResource: String?,
                      val hadis: String?,
                      val hadisResource: String?,
                      val esmaulhusna: String?,
                      val meaning: String?,
                      val hikmetname: String?,
                      val name: String?,
                      val babyName: String?,
                      val babyNameMeaning: String?,
                      val duaKardesligiDate: String?,
                      val duaKardesligiDua: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mArrayList : ArrayList<String>? = null

    inner class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun ModelViewHolder() {
            }
        }

    inner class ModelViewHolder1(view: View) : RecyclerView.ViewHolder(view) {

        val scrollView : HorizontalScrollView = view.findViewById(R.id.scrollViewHorizontal)

        val imsakTimeTxt : TextView = view.findViewById(R.id.imsak_time_txt)
        val gunesTimeTxt : TextView = view.findViewById(R.id.gunesTimeTxt)
        val ogleTimeTxt : TextView = view.findViewById(R.id.ogleTimeTxt)
        val ikindiTimeTxt : TextView = view.findViewById(R.id.ikindiTimeTxt)
        val aksamTimeTxt : TextView = view.findViewById(R.id.aksamTimeTxt)
        val yatsiTimeTxt : TextView = view.findViewById(R.id.yatsiTimeTxt)

        val cardImsak : CardView = view.findViewById(R.id.cardClockImsak)
        val cardGunes : CardView = view.findViewById(R.id.cardClockGunes)
        val cardOgle : CardView = view.findViewById(R.id.cardClockOgle)
        val cardIkindi : CardView = view.findViewById(R.id.cardClockIkindi)
        val cardAksam : CardView = view.findViewById(R.id.cardClockAksam)
        val cardYatsi : CardView = view.findViewById(R.id.cardClockYatsi)

        fun ModelViewHolder1(position: Int) {

            scrollView.post {
                scrollView.fullScroll(View.FOCUS_LEFT)
            }

            imsakTimeTxt.setText(list?.get(0))
            gunesTimeTxt.setText(list?.get(1))
            ogleTimeTxt.setText(list?.get(2))
            ikindiTimeTxt.setText(list?.get(3))
            aksamTimeTxt.setText(list?.get(4))
            yatsiTimeTxt.setText(list?.get(5))

            setPrayTimeBg(remaningPrayTime!!,afterYatsi)
        }

        private fun setPrayTimeBg(remainingPrayName: String, afterYatsi: Boolean?) {

            when (remainingPrayName) {
                Constant.IMSAK -> {
                    if (afterYatsi != true) {

                        cardImsak.setCardBackgroundColor(Color.rgb(43,209,227))
                    }

                    cardYatsi.setCardBackgroundColor(Color.TRANSPARENT)

                }

                Constant.GUNES -> {

                    cardImsak.setCardBackgroundColor(Color.TRANSPARENT)

                    cardGunes.setCardBackgroundColor(Color.rgb(43,209,227))
                }

                Constant.OGLE -> {

                    cardGunes.setCardBackgroundColor(Color.TRANSPARENT)

                    cardOgle.setCardBackgroundColor(Color.rgb(43,209,227))
                }

                Constant.IKINDI -> {

                    cardOgle.setCardBackgroundColor(Color.TRANSPARENT)

                    cardIkindi.setCardBackgroundColor(Color.rgb(43,209,227))
                }

                Constant.AKSAM -> {

                    cardIkindi.setCardBackgroundColor(Color.TRANSPARENT)

                    cardAksam.setCardBackgroundColor(Color.rgb(43,209,227))
                }

                Constant.YATSI -> {

                    cardAksam.setCardBackgroundColor(Color.TRANSPARENT)

                    cardYatsi.setCardBackgroundColor(Color.rgb(43,209,227))
                }
            }
        }

    }

    inner class ModelViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        val cardKuran : CardView = view.findViewById(R.id.cardKuran)
        val cardZikirmatik : CardView = view.findViewById(R.id.cardZikirmatik)
        val cardPusula : CardView = view.findViewById(R.id.cardPusula)

        fun ModelViewHolder2(position: Int) {

            cardKuran.setOnClickListener {
                Toast.makeText(mycontext,"Yakında Kur-an'ı Kerim sayfası eklenecektir.",Toast.LENGTH_SHORT).show()
            }

            cardZikirmatik.setOnClickListener {
                val intent = Intent(mycontext, Zikirmatik::class.java)
                mycontext.startActivity(intent)
            }

            cardPusula.setOnClickListener {
                val intent = Intent(mycontext, Kible::class.java)
                mycontext.startActivity(intent)
            }
        }
    }

    inner class ModelViewHolder3(view: View) : RecyclerView.ViewHolder(view) {

        val txtDuaAyet : TextView = view.findViewById(R.id.txtDuaInDuaAyetLayout)
        val txtResource : TextView = view.findViewById(R.id.txtResource)
        val txtHeader : TextView = view.findViewById(R.id.txtHeaderInDuaAyetLayout)

        val btnShare : Button = view.findViewById(R.id.share_hadisdua_btn)
        val image : ImageView = view.findViewById(R.id.imageInDuaAyetLayout)

        fun ModelViewHolder3(position: Int,duaAyet : String?, resource:  String?,imageResource : Int,header : String) {

            txtDuaAyet.setText(duaAyet)
            txtResource.setText(resource)
            txtHeader.setText(header)

            image.setImageResource(imageResource)

            btnShare.setOnClickListener {
                share("${duaAyet} / ${resource}")
            }

        }
        private fun share(text: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, mycontext.resources.getString(R.string.app_name))
            intent.putExtra(
                Intent.EXTRA_TEXT,
                text
            )
            intent.type = "text/plain"
            mycontext.startActivity(intent)
        }
    }

    inner class ModelViewHolder4(view: View) : RecyclerView.ViewHolder(view) {

        val txtEsmaUlHusna : TextView = view.findViewById(R.id.txtEsmaUlHusna)
        val txtMean : TextView = view.findViewById(R.id.txtMean)

        val btnShare : Button = view.findViewById(R.id.share_esmaulhusna_btn)

        fun ModelViewHolder4(position: Int,esmaulhusna :String?, meaning: String?) {
            txtEsmaUlHusna.setText(esmaulhusna)
            txtMean.setText(meaning)

            btnShare.setOnClickListener {
                share("Yüce Allah'ın isimlerinden olan ${esmaulhusna} anlamı: ${meaning}")
            }
        }

        private fun share(text: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, mycontext.resources.getString(R.string.app_name))
            intent.putExtra(
                Intent.EXTRA_TEXT,
                text
            )
            intent.type = "text/plain"
            mycontext.startActivity(intent)
        }
    }

    inner class ModelViewHolder5(view: View) : RecyclerView.ViewHolder(view) {

        val txtDua : TextView = view.findViewById(R.id.txtDua)
        val txtDate: TextView = view.findViewById(R.id.txtDate)

        val btnHide : Button = view.findViewById(R.id.btnHide)
        val btnShuffle : ImageButton = view.findViewById(R.id.imagebtnShuffle)
        val btnPrayed : Button = view.findViewById(R.id.btnPrayed)

        fun ModelViewHolder5(position: Int) {

        }
    }

    inner class ModelViewHolder6(view: View) : RecyclerView.ViewHolder(view) {

        val childNameTxt : TextView = view.findViewById(R.id.child_name)
        val nameMeaningTxt: TextView = view.findViewById(R.id.child_name_meaning)

        fun ModelViewHolder6(position: Int, child:String?, meaning: String?) {
            childNameTxt.text = child
            nameMeaningTxt.text = meaning
        }
    }
    inner class ModelViewHolder7(view: View) : RecyclerView.ViewHolder(view) {

        val hikmetnameTxt : TextView = view.findViewById(R.id.txtHikmetText)
        val nameTxt: TextView = view.findViewById(R.id.txtKaynakHikmet)

        fun ModelViewHolder7(position: Int,name : String?,hikmetname: String?) {
            hikmetnameTxt.text = hikmetname
            nameTxt.text = name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                0 -> ModelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.namaz_saatleri_text_layout, parent, false))
                1 -> ModelViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.namaz_ssatleri_layout, parent, false))
                2 -> ModelViewHolder2(LayoutInflater.from(parent.context).inflate(R.layout.kuran_hadis_ramazan_layout, parent, false))
                3 -> ModelViewHolder3(LayoutInflater.from(parent.context).inflate(R.layout.gunun_duasi_ayeti_layout, parent, false))
                4 -> ModelViewHolder7(LayoutInflater.from(parent.context).inflate(R.layout.hikmetname_layout, parent, false))
                5 -> ModelViewHolder3(LayoutInflater.from(parent.context).inflate(R.layout.gunun_duasi_ayeti_layout, parent, false))
                6 -> ModelViewHolder3(LayoutInflater.from(parent.context).inflate(R.layout.gunun_duasi_ayeti_layout, parent, false))
                7 -> ModelViewHolder4(LayoutInflater.from(parent.context).inflate(R.layout.esmaulhusna_layout, parent, false))
                8 -> ModelViewHolder5(LayoutInflater.from(parent.context).inflate(R.layout.dua_kardesligi_layout, parent, false))
                9 -> ModelViewHolder6(LayoutInflater.from(parent.context).inflate(R.layout.child_name_layout, parent, false))
                else -> ModelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.empty_bottom_layout, parent, false))
            }
        }

        override fun getItemCount(): Int {
            return 10
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0 -> {
                val holders: ModelViewHolder = holder as ModelViewHolder
                holders.ModelViewHolder()
            }
            1 -> {
                val viewHolder0: ModelViewHolder1 = holder as ModelViewHolder1
                viewHolder0.ModelViewHolder1(position)
            }
            2 -> {
                val viewHolder0: ModelViewHolder2 = holder as ModelViewHolder2
                viewHolder0.ModelViewHolder2(position)
            }
            3 -> {
                val viewHolder0: ModelViewHolder3 = holder as ModelViewHolder3
                viewHolder0.ModelViewHolder3(position,dua, duaResource, R.drawable.dua_png,"Günün Duası")
            }
            4 -> {
                    val viewHolder0: ModelViewHolder7 = holder as ModelViewHolder7
                    viewHolder0.ModelViewHolder7(position,name,hikmetname)
            }
            5 -> {
                val viewHolder0: ModelViewHolder3 = holder as ModelViewHolder3
                viewHolder0.ModelViewHolder3(position,ayet, ayetResource, R.drawable.quran,"Günün Ayeti")
            }
            6 -> {
                val viewHolder0: ModelViewHolder3 = holder as ModelViewHolder3
                viewHolder0.ModelViewHolder3(position,hadis,hadisResource,R.drawable.flower,"Günün Hadisi")
            }
            7 -> {
                val viewHolder0: ModelViewHolder4 = holder as ModelViewHolder4
                viewHolder0.ModelViewHolder4(position,esmaulhusna, meaning)
            }
            8 -> {
                val viewHolder0: ModelViewHolder5 = holder as ModelViewHolder5
                viewHolder0.ModelViewHolder5(position)
            }
            9 -> {
                val viewHolder0: ModelViewHolder6 = holder as ModelViewHolder6
                viewHolder0.ModelViewHolder6(position,babyName,babyNameMeaning)
            }
        }
    }

    fun updateData(array: ArrayList<String>?) {
        mArrayList = ArrayList()
        mArrayList?.addAll(array!!)
    }

}
