package com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge

import com.namazvakitleri.internetsiz.modal.BasicReligiousKnowledge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasicReligiousKnowledgeRepository @Inject constructor(){


    fun getKnowledge(): Flow<ArrayList<BasicReligiousKnowledge>> {

        return flow {
            emit(prepareListData())
        }
    }


    private fun prepareListData(): ArrayList<BasicReligiousKnowledge> {

        var knowledge = ArrayList<BasicReligiousKnowledge>()

        var listDataHeader: ArrayList<String> = ArrayList()
        var listDataChild: HashMap<String, List<String>> = HashMap()


        listDataHeader.add("İman Nedir ?")
        listDataHeader.add("Kelime-i Tevhid Nedir ?")
        listDataHeader.add("Kelime-i Şehadet Nedir ?")
        listDataHeader.add("İslam'ın Şartları Nelerdir ?")
        listDataHeader.add("İman'ın Şartları Nelerdir ?")
        listDataHeader.add("32 Farz Nelerdir ?")
        listDataHeader.add("Namazın Farzları Nelerdir ?")
        listDataHeader.add("Abdestin Farzları Nelerdir ?")

        val iman: MutableList<String> = ArrayList()
        val kelimeiTevhid: MutableList<String> = ArrayList()
        val kelimeiSehadet: MutableList<String> = ArrayList()
        val islamSartlari: MutableList<String> = ArrayList()
        val imanSartlari: MutableList<String> = ArrayList()
        val otuzIkiFarz: MutableList<String> = ArrayList()
        val namazinFarzlari: MutableList<String> = ArrayList()
        val abdestinFarzlari: MutableList<String> = ArrayList()

        iman.add("İman kelimesi sözlükte inanmak, güvenmek, bir şeyin doğruluğunu kabul " +
                "etmek ve onaylamak gibi anlamlara gelir. Dinî bir kavram olarak ise iman; " +
                "Yüce Allah’tan Hz. Muhammed’e (S.A.V.) indirilen bütün bilgi, mesaj ve " +
                "hükümlerin doğruluğunu hiçbir şüphe duymadan kabul etmek demektir.")
        kelimeiTevhid.add("Allah’tan başka ilah yoktur. Muhammed(S.A.V.), Allah’ın Resulüdür.\n" +
                "…Muhammed, Allah’ın resulü ve nebilerin sonuncusudur...(Ahzâb suresi, 40.ayet)")
        kelimeiSehadet.add("Şehadet bir şeye tanık olmak, bir şeyin doğruluğundan kesin bir şekilde emin olmak " +
                "gibi anlamlara gelir.\nKelime-i Şehadet: Ben şehadet ederim ki Allah’tan başka ilah yoktur. Ve yine " +
                "şehadet ederim ki Muhammed(S.A.V.) O’nun kulu ve resulüdür ")
        islamSartlari.add("1-) Kelime-i Şehadet getirmek\n2-) Namaz Kılmak\n3-) Oruç tutmak\n4-) Zekât vermek\n5-) Hacca gitmek")
        imanSartlari.add("1-) Allah'a inanmak\n2-) Peygamberlere inanmak\n3-) Meleklere inanmak\n4-) Semavî kitaplara inanmak\n5-) Ahirete inanmak\n6-) Kazâ ve Kader'in Allah'tan olduğuna inanmak")
        otuzIkiFarz.add("1-) İmanın Şartı (6)\n2-) İslamın Şartı (5)\n3-) Namazın Farzı (12)\n4-) Abdestin Farzı (4)\n5-) Guslün Farzı (3)\n6-) Teyemmümün Farzı(2)")
        namazinFarzlari.add("A- Dışındaki farzları altıdır.\n" +
                "   1- Hadesten taharet.\n" +
                "   2- Necasetten taharet.\n" +
                "   3- Setr-i avret.\n" +
                "   4- İstikbal-i Kıble.\n" +
                "   5- Vakit.\n" +
                "   6- Niyet.\n" +
                "B- İçindeki farzları da altıdır.\n" +
                "   1- İftitah veya Tahrime tekbiri.\n" +
                "   2- Kıyam.\n" +
                "   3- Kıraat.\n" +
                "   4- Rüku.\n" +
                "   5- Secde.\n" +
                "   6- Ka’de-i ahire.")
        abdestinFarzlari.add("1- Abdest alırken yüzü yıkamak.\n" +
                "2- Elleri dirsekleri ile birlikte yıkamak.\n" +
                "3- Başın dörtte birini mesh etmek.\n" +
                "4- Ayakları topukları ile birlikte yıkamak.")

        listDataChild[listDataHeader[0]] = iman
        listDataChild[listDataHeader[1]] = kelimeiTevhid
        listDataChild[listDataHeader[2]] = kelimeiSehadet
        listDataChild[listDataHeader[3]] = islamSartlari
        listDataChild[listDataHeader[4]] = imanSartlari
        listDataChild[listDataHeader[5]] = otuzIkiFarz
        listDataChild[listDataHeader[6]] = namazinFarzlari
        listDataChild[listDataHeader[7]] = abdestinFarzlari

        knowledge.add(BasicReligiousKnowledge(listDataHeader, listDataChild))

        return knowledge
    }
}