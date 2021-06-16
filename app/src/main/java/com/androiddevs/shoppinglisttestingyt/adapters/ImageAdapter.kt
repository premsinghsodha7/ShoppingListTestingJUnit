package com.androiddevs.shoppinglisttestingyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_image.view.*
import java.util.zip.Inflater
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemViw: View): RecyclerView.ViewHolder(itemViw)

    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_image,
            parent,
            false
        ))
    }

    private var onItemClickListner: ((String) -> Unit)? = null

    fun setOnItemClickListner(listner: (String)-> Unit){
        onItemClickListner = listner
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        holder.itemView.apply {
            glide.load(url).into(ivShoppingImage)

            setOnClickListener {
                onItemClickListner?.let {click ->
                    click(url)
                }
            }
        }
    }

    override fun getItemCount(): Int = images.size

}