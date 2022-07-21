package com.namazvakitleri.internetsiz.dialog

import android.app.Dialog
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.namazvakitleri.internetsiz.Application
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.databinding.DialogNotificationSoundBinding
import com.namazvakitleri.internetsiz.utils.constant.Constant
import com.namazvakitleri.internetsiz.utils.extension.get
import com.namazvakitleri.internetsiz.utils.extension.put
import com.namazvakitleri.internetsiz.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationSoundDialog : DialogFragment(), View.OnClickListener, MediaPlayer.OnCompletionListener{

    private var binding: DialogNotificationSoundBinding? = null


    private lateinit var sabaSound: MediaPlayer
    private lateinit var rastSound: MediaPlayer
    private lateinit var segahSound: MediaPlayer
    private lateinit var standartSound: MediaPlayer
    private lateinit var beepSound: MediaPlayer

    private var soundSelectId: Int = -1
    private lateinit var soundSelectName: String

    @Inject lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (binding == null) {
            binding = DialogNotificationSoundBinding.inflate(inflater,container,false)
        }

        init()

        binding?.sabaBtn?.setOnClickListener(this)
        binding?.rastBtn?.setOnClickListener(this)
        binding?.segahBtn?.setOnClickListener(this)
        binding?.standartBtn?.setOnClickListener(this)
        binding?.beepBtn?.setOnClickListener(this)

        sabaSound.setOnCompletionListener(this)
        rastSound.setOnCompletionListener(this)
        segahSound.setOnCompletionListener(this)
        standartSound.setOnCompletionListener(this)
        beepSound.setOnCompletionListener(this)


        binding?.sabaCbx?.setOnCheckedChangeListener { _, isChecked ->

            prefs.put(Constant.SELECT_SOUND_ID, R.raw.saba)
            prefs.put(Constant.SELECT_SOUND_NAME, Constant.SABA)


            binding?.rastCbx?.isEnabled = !isChecked
            binding?.segahCbx?.isEnabled = !isChecked
            binding?.standartCbx?.isEnabled = !isChecked
            binding?.beepCbx?.isEnabled = !isChecked

        }
        binding?.rastCbx?.setOnCheckedChangeListener { _, isChecked ->

            prefs.put(Constant.SELECT_SOUND_ID, R.raw.rast)
            prefs.put(Constant.SELECT_SOUND_NAME, Constant.RAST)

            binding?.segahCbx?.isEnabled = !isChecked
            binding?.standartCbx?.isEnabled = !isChecked
            binding?.beepCbx?.isEnabled = !isChecked
            binding?.sabaCbx?.isEnabled = !isChecked
        }
        binding?.segahCbx?.setOnCheckedChangeListener { _, isChecked ->

            prefs.put(Constant.SELECT_SOUND_ID,R.raw.segah)
            prefs.put(Constant.SELECT_SOUND_NAME, Constant.SEGAH)

            binding?.rastCbx?.isEnabled = !isChecked
            binding?.standartCbx?.isEnabled = !isChecked
            binding?.beepCbx?.isEnabled = !isChecked
            binding?.sabaCbx?.isEnabled = !isChecked
        }
        binding?.standartCbx?.setOnCheckedChangeListener { _, isChecked ->

            prefs.put(Constant.SELECT_SOUND_ID, R.raw.standart)
            prefs.put(Constant.SELECT_SOUND_NAME, Constant.STANDART)

            binding?.rastCbx?.isEnabled = !isChecked
            binding?.segahCbx?.isEnabled = !isChecked
            binding?.beepCbx?.isEnabled = !isChecked
            binding?.sabaCbx?.isEnabled = !isChecked
        }
        binding?.beepCbx?.setOnCheckedChangeListener { _, isChecked ->

            prefs.put(Constant.SELECT_SOUND_ID, R.raw.beep)
            prefs.put(Constant.SELECT_SOUND_NAME, Constant.BEEP)

            binding?.rastCbx?.isEnabled = !isChecked
            binding?.segahCbx?.isEnabled = !isChecked
            binding?.standartCbx?.isEnabled = !isChecked
            binding?.sabaCbx?.isEnabled = !isChecked
        }

        return  binding?.root
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    private fun init() {
        sabaSound = MediaPlayer.create(Application.context, R.raw.saba)
        rastSound = MediaPlayer.create(Application.context, R.raw.rast)
        segahSound = MediaPlayer.create(Application.context,R.raw.segah)
        standartSound = MediaPlayer.create(Application.context, R.raw.standart)
        beepSound = MediaPlayer.create(Application.context, R.raw.beep)

        soundSelectId = prefs.get(Constant.SELECT_SOUND_ID, R.raw.standart)
        soundSelectName = prefs.get(Constant.SELECT_SOUND_NAME, Constant.STANDART)

        setChecked(soundSelectName)
    }

    private fun setChecked(soundSelectName: String) {

        when(soundSelectName) {
            Constant.SABA -> {
                binding?.sabaCbx?.isChecked = true

                binding?.rastCbx?.isEnabled = false
                binding?.segahCbx?.isEnabled = false
                binding?.standartCbx?.isEnabled = false
                binding?.beepCbx?.isEnabled = false
            }
            Constant.RAST -> {
                binding?.rastCbx?.isChecked = true


                binding?.segahCbx?.isEnabled = false
                binding?.standartCbx?.isEnabled = false
                binding?.beepCbx?.isEnabled = false
                binding?.sabaCbx?.isEnabled = false
            }
            Constant.SEGAH -> {
                binding?.segahCbx?.isChecked = true

                binding?.rastCbx?.isEnabled = false
                binding?.standartCbx?.isEnabled = false
                binding?.beepCbx?.isEnabled = false
                binding?.sabaCbx?.isEnabled = false
            }
            Constant.STANDART -> {
                binding?.standartCbx?.isChecked = true


                binding?.rastCbx?.isEnabled = false
                binding?.segahCbx?.isEnabled = false
                binding?.beepCbx?.isEnabled = false
                binding?.sabaCbx?.isEnabled = false


            }
            Constant.BEEP -> {
                binding?.beepCbx?.isChecked = true


                binding?.rastCbx?.isEnabled = false
                binding?.segahCbx?.isEnabled = false
                binding?.standartCbx?.isEnabled = false
                binding?.sabaCbx?.isEnabled = false

            }
        }
    }


    override fun onDestroy() {

        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.saba_btn -> {
                when {
                    sabaSound.isPlaying -> {
                        mediaPlayerSettings(sabaSound,null,null, binding!!.sabaBtn, binding!!.sabaBtn)
                    }
                    rastSound.isPlaying -> {
                        mediaPlayerSettings(rastSound,sabaSound,true, binding!!.rastBtn, binding!!.sabaBtn)
                    }
                    segahSound.isPlaying -> {
                        mediaPlayerSettings(segahSound,sabaSound,true, binding!!.segahBtn, binding!!.sabaBtn)
                    }
                    standartSound.isPlaying -> {
                        mediaPlayerSettings(standartSound,sabaSound,true, binding!!.standartBtn, binding!!.sabaBtn)
                    }
                    beepSound.isPlaying -> {
                        mediaPlayerSettings(beepSound,sabaSound,true, binding!!.beepBtn, binding!!.sabaBtn)
                    }
                    else -> {
                        sabaSound.start()
                        binding?.sabaBtn?.setBackgroundResource(R.drawable.icon_pause)
                    }
                }
                Application.context.showToast("Ezanı okuyan kişi: Ali Tel")

            }
            R.id.rast_btn -> {
                when {
                    rastSound.isPlaying -> {
                        mediaPlayerSettings(rastSound, null, null, binding!!.rastBtn, binding!!.rastBtn)
                    }
                    sabaSound.isPlaying -> {
                        mediaPlayerSettings(sabaSound,rastSound,true, binding!!.sabaBtn, binding!!.rastBtn)
                    }
                    segahSound.isPlaying -> {
                        mediaPlayerSettings(segahSound,rastSound,true, binding!!.segahBtn, binding!!.rastBtn)
                    }
                    standartSound.isPlaying -> {
                        mediaPlayerSettings(standartSound,rastSound,true, binding!!.standartBtn, binding!!.rastBtn)
                    }
                    beepSound.isPlaying -> {
                        mediaPlayerSettings(beepSound,rastSound,true, binding!!.beepBtn, binding!!.rastBtn)
                    }
                    else -> {
                        rastSound.start()
                        binding?.rastBtn?.setBackgroundResource(R.drawable.icon_pause)

                    }
                }
                Application.context.showToast("Ezanı okuyan kişi: Nurettin Okumuş")
            }
            R.id.segah_btn -> {
                when {
                    segahSound.isPlaying -> {
                        mediaPlayerSettings(segahSound, null, null, binding!!.segahBtn, binding!!.segahBtn)
                    }
                    sabaSound.isPlaying -> {
                        mediaPlayerSettings(sabaSound, segahSound, true, binding!!.sabaBtn, binding!!.segahBtn)
                    }
                    rastSound.isPlaying -> {
                        mediaPlayerSettings(rastSound, segahSound, true, binding!!.rastBtn, binding!!.segahBtn)
                    }
                    standartSound.isPlaying -> {
                        mediaPlayerSettings(standartSound, segahSound, true, binding!!.standartBtn, binding!!.segahBtn)
                    }
                    beepSound.isPlaying -> {
                        mediaPlayerSettings(beepSound, segahSound, true, binding!!.beepBtn, binding!!.segahBtn)
                    }
                    else -> {
                        segahSound.start()
                        binding?.segahBtn?.setBackgroundResource(R.drawable.icon_pause)
                    }
                }
                Application.context.showToast("Ezanı okuyan kişi: Ali Tel")
            }
            R.id.standart_btn -> {
                when {
                    standartSound.isPlaying -> {
                        mediaPlayerSettings(standartSound, null, null, binding!!.standartBtn, binding!!.standartBtn)
                    }
                    sabaSound.isPlaying -> {
                        mediaPlayerSettings(sabaSound, standartSound, true, binding!!.sabaBtn, binding!!.standartBtn)
                    }
                    rastSound.isPlaying -> {
                        mediaPlayerSettings(rastSound, standartSound, true, binding!!.rastBtn, binding!!.standartBtn)
                    }
                    segahSound.isPlaying -> {
                        mediaPlayerSettings(segahSound, standartSound, true, binding!!.segahBtn, binding!!.standartBtn)
                    }
                    beepSound.isPlaying -> {
                        mediaPlayerSettings(beepSound, standartSound, true, binding!!.beepBtn, binding!!.standartBtn)
                    }
                    else -> {
                        standartSound.start()
                        binding?.standartBtn?.setBackgroundResource(R.drawable.icon_pause)
                    }
                }
            }
            R.id.beep_btn -> {
                when {
                    beepSound.isPlaying -> {
                        mediaPlayerSettings(beepSound, null, null, binding!!.beepBtn, binding!!.beepBtn)
                    }
                    sabaSound.isPlaying -> {
                        mediaPlayerSettings(sabaSound, beepSound, true, binding!!.sabaBtn, binding!!.beepBtn)
                    }
                    rastSound.isPlaying -> {
                        mediaPlayerSettings(rastSound, beepSound, true, binding!!.rastBtn, binding!!.beepBtn)
                    }
                    segahSound.isPlaying -> {
                        mediaPlayerSettings(segahSound, beepSound, true, binding!!.segahBtn, binding!!.beepBtn)
                    }
                    standartSound.isPlaying -> {
                        mediaPlayerSettings(standartSound, beepSound, true, binding!!.standartBtn, binding!!.beepBtn)
                    }
                    else -> {
                        beepSound.start()
                        binding?.beepBtn?.setBackgroundResource(R.drawable.icon_pause)
                    }
                }
            }
        }
    }

    private fun mediaPlayerSettings(mediaPlayer: MediaPlayer, playSound: MediaPlayer? = null, start: Boolean? = null, playBtn: Button, pauseBtn: Button) {

        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        playBtn.setBackgroundResource(R.drawable.icon_play)


        if (start != null) {
            playSound?.start()
            pauseBtn.setBackgroundResource(R.drawable.icon_pause)
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {

        when(p0) {
            sabaSound -> {
                binding?.sabaBtn?.setBackgroundResource(R.drawable.icon_play)

            }
            rastSound -> {
                binding?.rastBtn?.setBackgroundResource(R.drawable.icon_play)

            }
            segahSound -> {
                binding?.segahBtn?.setBackgroundResource(R.drawable.icon_play)

            }
            standartSound -> {
                binding?.standartBtn?.setBackgroundResource(R.drawable.icon_play)

            }
            beepSound -> {
                binding?.beepBtn?.setBackgroundResource(R.drawable.icon_play)

            }
        }
    }
}
















