package com.potentialServices.passwordmanager.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.databinding.PasswordItemBinding
import com.potentialServices.passwordmanager.models.PasswordItem



/** Adapter for the recycler view of the passwords **/
class PasswordAdapter(private val passwordList:List<PasswordItem>, private val onClickItem: OnClickItem):RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {


    /** password view model  **/
    inner class  PasswordViewHolder(val passwordItemBinding: PasswordItemBinding) :RecyclerView.ViewHolder(passwordItemBinding.root)



    /** to copy text we must need a context   **/
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        if (context == null)
            context = parent.context
        return PasswordViewHolder(
            PasswordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return  passwordList.size
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.passwordItemBinding.apply {
//            this.root.setOnLongClickListener {
//                it.scaleX = 1.5f
//                it.scaleY = 1.5f
//                true
//            }
            this.root.setOnClickListener {
                onClickItem.onClick(passwordList[position].id)

            }
            /** password binding scope  **/
            mainIcon.setImageResource(passwordList[position].passwordUsed)
            username.text=passwordList[position].userName
            setBack(likeIv,passwordList[position])
            editIcon.setOnClickListener {
                onClickItem.deleteItem(passwordList[position])
            }
            title.text = passwordList[position].title
            wesite.text= passwordList[position].website
            copyIcon.setOnClickListener {
                  setClipboard(context!!,passwordList[position])
            }
           likeIv.setOnClickListener {
             setLike(likeIv,passwordList[position])
//                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show()
            }

        }
    }

    /** On click interface  **/
    interface OnClickItem{
        fun onClick(index:Int)
        fun onUpdate(passwordItem: PasswordItem,flag:Boolean)

        fun onCopy(passwordItem: PasswordItem)

        fun deleteItem(passwordItem: PasswordItem)
    }


    /** function to copy  the text to the clipBoard  **/
    private fun setClipboard(context: Context, passwordItem: PasswordItem) {
        onClickItem.onCopy(passwordItem)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = passwordItem.password
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", passwordItem.password)
            clipboard.setPrimaryClip(clip)
        }
    }


    /** function to set the like  **/
    private fun setLike(view: ImageView, passwordItem:PasswordItem){
        if(passwordItem.liked){
//            view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.black) )
            onClickItem.onUpdate(passwordItem,false)
        }else{
//            view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.white) )
           onClickItem.onUpdate(passwordItem,true)
        }


    }


    /** function to set the color of the like image  **/
     private fun setBack(view: ImageView, passwordItem:PasswordItem){

        if(passwordItem.liked){
            view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.like_color) )
        }else{
            view.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.black) )
        }
     }
}